package com.example.kadai_002.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.Role;
import com.example.kadai_002.entity.Users;
import com.example.kadai_002.form.PasswordResetForm;
import com.example.kadai_002.form.SignupForm;
import com.example.kadai_002.form.UserEditForm;
import com.example.kadai_002.repository.RoleRepository;
import com.example.kadai_002.repository.UsersRepository;

@Service
public class UsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository,
                        RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Users create(SignupForm signupForm) {
        Users users = new Users();

        // ROLE_GENERAL を利用（無料会員）
        Role role = roleRepository.findByName("ROLE_GENERAL")
                .orElseThrow(() -> new IllegalArgumentException("Role 'ROLE_GENERAL' not found"));

        users.setUserName(signupForm.getName());
        users.setFurigana(signupForm.getFurigana());
        users.setUserPostCode(signupForm.getPostalCode());
        users.setUserAddress(signupForm.getAddress());
        users.setUserPhoneNumber(signupForm.getPhoneNumber());
        users.setMailAddress(signupForm.getEmail());
        users.setUserPassword(passwordEncoder.encode(signupForm.getPassword()));
        users.setRole(role);
        users.setEnabled(false);

        return usersRepository.save(users);
    }

    @Transactional
    public void update(UserEditForm userEditForm) {
        Users users = usersRepository.findById(userEditForm.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userEditForm.getId()));

        users.setUserName(userEditForm.getUserName());
        users.setFurigana(userEditForm.getFurigana());
        users.setUserPostCode(userEditForm.getUserPostCode());
        users.setUserAddress(userEditForm.getUserAddress());
        users.setUserPhoneNumber(userEditForm.getUserPhoneNumber());
        users.setMailAddress(userEditForm.getMailAddress());

        usersRepository.save(users);
    }

    public boolean isEmailRegistered(String email) {
        return usersRepository.findByMailAddress(email) != null;
    }

    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    @Transactional
    public void enableUser(Users users) {
        users.setEnabled(true);
        usersRepository.save(users);
    }

    public boolean isEmailChanged(UserEditForm userEditForm) {
        Users currentUser = usersRepository.findById(userEditForm.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userEditForm.getId()));
        return !userEditForm.getMailAddress().equals(currentUser.getMailAddress());
    }

    @Transactional
    public void passwordUpdate(PasswordResetForm passwordResetForm) {
        Users users = usersRepository.findByMailAddress(passwordResetForm.getEmail());
        if (users == null) {
            throw new IllegalArgumentException("User with email " + passwordResetForm.getEmail() + " not found.");
        }

        users.setUserPassword(passwordEncoder.encode(passwordResetForm.getPassword()));
        users.setEnabled(true);

        usersRepository.save(users);
    }

    /**
     * ユーザーを無料会員(ROLE_GENERAL, ID=1)に変更する
     */
    @Transactional
    public void updateUserToFreePlan(String email) {
        Users user = usersRepository.findByMailAddress(email);
        if (user != null) {
            Role freeRole = roleRepository.findById(1)
                    .orElseThrow(() -> new IllegalArgumentException("Role ID=1 (FREE) not found"));

            user.setRole(freeRole);  // roles_id を 1 (無料会員) に変更
            usersRepository.save(user);
            usersRepository.flush();
            logger.info("User role updated to FREE for email: {}", email);
        } else {
            logger.warn("No user found with email: {}", email);
        }
    }

    /**
     * ユーザーのロールを任意のIDに変更する (例: 3=ROLE_PRIME)
     */
    @Transactional
    public void updateUserRole(String email, int newRoleId) {
        Users user = usersRepository.findByMailAddress(email);
        if (user != null) {
            Role currentRole = user.getRole();
            logger.info("Before update: User {} current role: {}", email, currentRole.getName());

            Role newRole = roleRepository.findById(newRoleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role ID=" + newRoleId + " not found"));
            if (currentRole.getId().equals(newRole.getId())) {
                logger.info("No update required: user {} already has role {}", email, newRole.getName());
            } else {
                user.setRole(newRole);
                usersRepository.save(user);
                usersRepository.flush();  // 明示的にDBへコミット
                logger.info("After update: User {} role updated to {}", email, newRole.getName());
            }
        } else {
            logger.warn("No user found with email: {}", email);
        }
    }

    /**
     * 認証情報のロールを更新するメソッド
     * ユーザー情報を再読み込みし、新しいロール情報で認証情報を更新する
     * @param newRole 付与する新しいロール（例："ROLE_PRIME"）
     * @param userDetailsService UserDetailsService の Bean
     */
    public void refreshAuthenticationByRole(String newRole, UserDetailsService userDetailsService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.warn("No authentication found in SecurityContextHolder.");
            return;
        }
        // ユーザー名から最新のユーザー情報を取得
        UserDetails updatedUser = userDetailsService.loadUserByUsername(authentication.getName());
        // 新しい権限リストを作成（ここでは newRole のみをセットする例）
        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<>();
        updatedAuthorities.add(new SimpleGrantedAuthority(newRole));
        // 新しい Authentication オブジェクトを生成して設定
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUser,
                authentication.getCredentials(),
                updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        logger.info("Authentication refreshed with new role: {}", newRole);
    }
}
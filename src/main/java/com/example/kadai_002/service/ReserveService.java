package com.example.kadai_002.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.Reserve;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.entity.Users;
import com.example.kadai_002.form.ReserveRegisterForm;
import com.example.kadai_002.repository.ReserveRepository;
import com.example.kadai_002.repository.StoresRepository;
import com.example.kadai_002.repository.UsersRepository;

@Service
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final StoresRepository storesRepository;
    private final UsersRepository usersRepository;

    public ReserveService(ReserveRepository reserveRepository, StoresRepository storesRepository, UsersRepository usersRepository) {
        this.reserveRepository = reserveRepository;
        this.storesRepository = storesRepository;
        this.usersRepository = usersRepository;
    }

    // 営業時間内かどうかチェック
    public boolean isWithinBusinessHours(LocalTime checkinTime, LocalTime startTime, LocalTime endTime) {
        return !checkinTime.isBefore(startTime) && !checkinTime.isAfter(endTime);
    }

    // 予約人数が定員以下かどうかをチェックする
    public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {
        return numberOfPeople <= capacity;
    }

    @Transactional
    public void create(ReserveRegisterForm reserveRegisterForm) {
        try {
            // 店舗情報を取得
            Stores stores = storesRepository.findById(reserveRegisterForm.getHouseId())
                    .orElseThrow(() -> new IllegalArgumentException("店舗が見つかりません: " + reserveRegisterForm.getHouseId()));

            // ユーザー情報を取得
            Users users = usersRepository.findById(reserveRegisterForm.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません: " + reserveRegisterForm.getUserId()));

            // 日時情報を解析
            LocalDate checkinDate = LocalDate.parse(reserveRegisterForm.getCheckinDate());
            LocalTime checkinTime = LocalTime.parse(reserveRegisterForm.getCheckinTime());

            // デバッグログ
            System.out.println("Saving Reserve:");
            System.out.println("Store ID: " + stores.getId());
            System.out.println("User ID: " + users.getId());
            System.out.println("Checkin Date: " + checkinDate);
            System.out.println("Checkin Time: " + checkinTime);
            System.out.println("Number of People: " + reserveRegisterForm.getNumberOfPeople());

            // 予約オブジェクトを作成
            Reserve reserve = new Reserve();
            reserve.setStores(stores);
            reserve.setUsers(users);
            reserve.setCheckinDate(checkinDate);
            reserve.setCheckinTime(checkinTime);
            reserve.setNumberOfPeople(reserveRegisterForm.getNumberOfPeople());

            // 予約を保存
            reserveRepository.save(reserve);
        } catch (Exception e) {
            throw new RuntimeException("予約の保存中にエラーが発生しました。", e);
        }
    }
}
// Stripeの初期化
const stripe = Stripe('pk_test_51QJQLVFoKQ3torxMB1X4ARDwjc16EJx5v5E96bUy7nWAr0G5Yjeegh38FBEfXsTKzVbqLneYzcAEgmBARt1y4trL00ThM6o4D9');
const elements = stripe.elements();
const cardElement = elements.create('card');
cardElement.mount('#card-element');

// DOM要素の取得
const cardButton = document.getElementById('card-button');
const cardHolderName = document.getElementById('card-holder-name');
const cardError = document.getElementById('card-error');
const errorList = document.getElementById('error-list');
const cancelButton = document.getElementById('cancel-button');

// 【アップグレード処理】（登録処理）
cardButton.addEventListener('click', async (e) => {
    e.preventDefault();
    cardError.style.display = 'none';

    // エラーメッセージのクリア
    while (errorList.firstChild) {
        errorList.removeChild(errorList.firstChild);
    }

    // サーバーから動的にクライアントシークレットを取得
    const clientSecret = document.getElementById('client-secret').value;

    if (!clientSecret) {
        let li = document.createElement('li');
        li.textContent = 'クライアントシークレットがありません。再度試してください。';
        errorList.appendChild(li);
        cardError.style.display = 'block';
        return;
    }

    // Stripeにカード情報を送信してセットアップ
    const { setupIntent, error } = await stripe.confirmCardSetup(clientSecret, {
        payment_method: {
            card: cardElement,
            billing_details: { name: cardHolderName.value }
        }
    });

    if (error) {
        // エラーがあれば表示
        cardError.style.display = 'block';
        let li = document.createElement('li');
        li.textContent = error.message;
        errorList.appendChild(li);
    } else {
        // 成功時、サーバーにデータ送信（アップグレード処理へ）
        const form = document.getElementById('card-form');
        const hiddenInput = document.createElement('input');
        hiddenInput.setAttribute('type', 'hidden');
        hiddenInput.setAttribute('name', 'paymentMethodId');
        hiddenInput.setAttribute('value', setupIntent.payment_method);
        form.appendChild(hiddenInput);
        form.submit();
    }
});

// 【ダウングレード／キャンセル処理】（サブスクリプション管理画面の「キャンセル」ボタン）
cancelButton.addEventListener('click', (e) => {
    e.preventDefault();
    if (confirm('本当にキャンセルしますか？')) {
        // 別フローのキャンセル処理エンドポイントへリダイレクト
        window.location.href = '/cancel-subscription';
    }
});
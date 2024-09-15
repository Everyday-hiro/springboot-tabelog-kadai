function validatePasswords() {
	const currentPassword = document.querySelector('input[name="currentPassword"]').value;
	const newPassword = document.querySelector('input[name="newPassword"]').value;
	const errorMessage = document.getElementById('error-message');

	// パスワードの長さチェック
	if (currentPassword.length < 8 || currentPassword.length > 16 || newPassword.length < 8 || newPassword.length > 16) {
		errorMessage.textContent = 'パスワードは8文字以上16文字以下でお願いします。';
		return false; // フォーム送信を中止
	}

	errorMessage.textContent = ''; // エラーメッセージをクリア
	return true; // フォーム送信を許可
}
import CryptoJS from "crypto-js";

const hashUser = (user: string) => {
  const hashSecretKey = process.env.NEXT_PUBLIC_HASH_SECRET_KEY;
  if (!hashSecretKey) {
    throw new Error("HASH_SECRET_KEY is not defined");
  }

  // AES 암호화 수행
  const encrypted = CryptoJS.AES.encrypt(user, hashSecretKey).toString();
  return encrypted;
};

export default hashUser;

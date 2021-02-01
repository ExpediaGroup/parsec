(ns parsec.functions.digest-functions-test
  (:require [clojure.test :refer :all]
            [parsec.helpers :refer :all]
            [parsec.test-helpers :refer :all]
            [parsec.functions :refer :all]
            [parsec.functions.stringfunctions :refer :all]
            [clj-time.core :as time]))

(deftest adler32-test
  (let [adler32 (partial function-transform :adler32)]
    (testing "with nil"
      (is (nil? ((adler32 nil) {} nil))))
    (testing "with number"
      (is (thrown? Exception ((adler32 (long 0)) {} nil))))
    (testing "with string"
      (is (= "1a0b045d" ((adler32 "hello world") {} nil))))))

(deftest crc32-test
  (let [crc32 (partial function-transform :crc32)]
    (testing "with nil"
      (is (nil? ((crc32 nil) {} nil))))
    (testing "with number"
      (is (thrown? Exception ((crc32 (long 0)) {} nil))))
    (testing "with string"
      (is (= "0d4a1185" ((crc32 "hello world") {} nil))))))

(deftest md5-test
  (let [md5 (partial function-transform :md5)]
    (testing "with nil"
      (is (nil? ((md5 nil) {} nil)))
      (is (nil? ((md5 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((md5 (long 0)) {} nil)))
      (is (thrown? Exception ((md5 (int 1)) {} nil)))
      (is (thrown? Exception ((md5 1.0) {} nil)))
      (is (thrown? Exception ((md5 1/2) {} nil))))
    (testing "with string"
      (is (= "acbd18db4cc2f85cedef654fccc4a4d8" ((md5 "foo") {} nil))))
    (testing "with boolean"
      (is (thrown? Exception ((md5 false) {} nil)))
      (is (thrown? Exception ((md5 true) {} nil))))
    (testing "with date"
      (is (thrown? Exception ((md5 (time/today-at-midnight)) {} nil)))
      (is (thrown? Exception ((md5 (time/now)) {} nil))))))

(deftest sha256-test
  (let [sha256 (partial function-transform :sha256)]
    (testing "with nil"
      (is (nil? ((sha256 nil) {} nil)))
      (is (nil? ((sha256 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((sha256 (long 0)) {} nil)))
      (is (thrown? Exception ((sha256 (int 1)) {} nil)))
      (is (thrown? Exception ((sha256 1.0) {} nil)))
      (is (thrown? Exception ((sha256 1/2) {} nil))))
    (testing "with string"
      (is (= "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9" ((sha256 "hello world") {} nil))))
    (testing "with boolean"
      (is (thrown? Exception ((sha256 false) {} nil)))
      (is (thrown? Exception ((sha256 true) {} nil))))
    (testing "with date"
      (is (thrown? Exception ((sha256 (time/today-at-midnight)) {} nil)))
      (is (thrown? Exception ((sha256 (time/now)) {} nil))))))

(deftest hmac-sha256-test
  (let [hmac-sha256 (partial function-transform :hmac_sha256)]
    (testing "with nil"
      (is (nil? ((hmac-sha256 nil) {} nil)))
      (is (nil? ((hmac-sha256 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((hmac-sha256 (long 0)) {} nil)))
      (is (thrown? Exception ((hmac-sha256 (int 1)) {} nil)))
      (is (thrown? Exception ((hmac-sha256 1.0) {} nil)))
      (is (thrown? Exception ((hmac-sha256 1/2) {} nil))))
    (testing "with string"
      (is (= "734cc62f32841568f45715aeb9f4d7891324e6d948e4c6c60c0621cdac48623a" ((hmac-sha256 "hello world" "secret") {} nil))))
    (testing "with boolean"
      (is (thrown? Exception ((hmac-sha256 false) {} nil)))
      (is (thrown? Exception ((hmac-sha256 true) {} nil))))
    (testing "with date"
      (is (thrown? Exception ((hmac-sha256 (time/today-at-midnight)) {} nil)))
      (is (thrown? Exception ((hmac-sha256 (time/now)) {} nil))))))

(deftest sha384-test
  (let [sha384 (partial function-transform :sha384)]
    (testing "with nil"
      (is (nil? ((sha384 nil) {} nil))))
    (testing "with number"
      (is (thrown? Exception ((sha384 (long 0)) {} nil))))
    (testing "with string"
      (is (= "fdbd8e75a67f29f701a4e040385e2e23986303ea10239211af907fcbb83578b3e417cb71ce646efd0819dd8c088de1bd" ((sha384 "hello world") {} nil))))))

(deftest hmac-sha384-test
  (let [hmac-sha384 (partial function-transform :hmac_sha384)]
    (testing "with nil"
      (is (nil? ((hmac-sha384 nil) {} nil)))
      (is (nil? ((hmac-sha384 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((hmac-sha384 (long 0)) {} nil))))
    (testing "with string"
      (is (= "2da3bb177b92aae98c3ab22727d7f60c905be1baff71fb4b00a6e410923e6558376590c1faf922ff51ec49be77409ac6" ((hmac-sha384 "hello world" "secret") {} nil))))))

(deftest sha512-test
  (let [sha512 (partial function-transform :sha512)]
    (testing "with nil"
      (is (nil? ((sha512 nil) {} nil))))
    (testing "with number"
      (is (thrown? Exception ((sha512 (long 0)) {} nil))))
    (testing "with string"
      (is (= "309ecc489c12d6eb4cc40f50c902f2b4d0ed77ee511a7c7a9bcd3ca86d4cd86f989dd35bc5ff499670da34255b45b0cfd830e81f605dcf7dc5542e93ae9cd76f" ((sha512 "hello world") {} nil))))))

(deftest hmac-sha512-test
  (let [hmac-sha512 (partial function-transform :hmac_sha512)]
    (testing "with nil"
      (is (nil? ((hmac-sha512 nil) {} nil)))
      (is (nil? ((hmac-sha512 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((hmac-sha512 (long 0)) {} nil))))
    (testing "with string"
      (is (= "6d32239b01dd1750557211629313d95e4f4fcb8ee517e443990ac1afc7562bfd74ffa6118387efd9e168ff86d1da5cef4a55edc63cc4ba289c4c3a8b4f7bdfc2" ((hmac-sha512 "hello world" "secret") {} nil))))))

(deftest tiger-test
  (let [tiger (partial function-transform :tiger)]
    (testing "with nil"
      (is (nil? ((tiger nil) {} nil))))
    (testing "with number"
      (is (thrown? Exception ((tiger (long 0)) {} nil))))
    (testing "with string"
      (is (= "4c8fbddae0b6f25832af45e7c62811bb64ec3e43691e9cc3" ((tiger "hello world") {} nil))))))

(deftest hmac-tiger-test
  (let [hmac-tiger (partial function-transform :hmac_tiger)]
    (testing "with nil"
      (is (nil? ((hmac-tiger nil) {} nil)))
      (is (nil? ((hmac-tiger :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((hmac-tiger (long 0)) {} nil))))
    (testing "with string"
      (is (= "e4e7d5a746cde566aad3864cc00afd50469d195007aba89c" ((hmac-tiger "hello world" "secret") {} nil))))))

(deftest whirlpool-test
  (let [whirlpool (partial function-transform :whirlpool)]
    (testing "with nil"
      (is (nil? ((whirlpool nil) {} nil))))
    (testing "with number"
      (is (thrown? Exception ((whirlpool (long 0)) {} nil))))
    (testing "with string"
      (is (= "8d8309ca6af848095bcabaf9a53b1b6ce7f594c1434fd6e5177e7e5c20e76cd30936d8606e7f36acbef8978fea008e6400a975d51abe6ba4923178c7cf90c802" ((whirlpool "hello world") {} nil))))))

(deftest hmac-whirlpool-test
  (let [hmac-whirlpool (partial function-transform :hmac_whirlpool)]
    (testing "with nil"
      (is (nil? ((hmac-whirlpool nil) {} nil)))
      (is (nil? ((hmac-whirlpool :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((hmac-whirlpool (long 0)) {} nil))))
    (testing "with string"
      (is (= "39935d42ec5ec9c778e9e0ad05e53893296d82ca118ddb01021904ba88cb07c6925dbdd2a16beeed811a78f262839cee8015246a688d087431659772948f0ea8" ((hmac-whirlpool "hello world" "secret") {} nil))))))

(deftest siphash-test
  (let [siphash (partial function-transform :siphash)]
    (testing "with nil"
      (is (nil? ((siphash nil) {} nil)))
      (is (nil? ((siphash :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((siphash (long 0)) {} nil))))
    (testing "with string"
      (is (= "4c00f66010eb806c" ((siphash "hello world" "12345678abcdefgh") {} nil))))))

(deftest siphash48-test
  (let [siphash48 (partial function-transform :siphash48)]
    (testing "with nil"
      (is (nil? ((siphash48 nil) {} nil)))
      (is (nil? ((siphash48 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((siphash48 (long 0)) {} nil))))
    (testing "with string"
      (is (= "3392cccde5c3b774" ((siphash48 "hello world" "12345678abcdefgh") {} nil))))))

(deftest gost-test
  (let [gost (partial function-transform :gost)]
    (testing "with nil"
      (is (nil? ((gost nil) {} nil)))
      (is (nil? ((gost :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((gost (long 0)) {} nil))))
    (testing "with string"
      (is (= "c5aa1455afe9f0c440eec3c96ccccb5c8495097572cc0f625278bd0da5ea5e07" ((gost "hello world") {} nil))))))

(deftest hmac-gost-test
  (let [hmac-gost (partial function-transform :hmac_gost)]
    (testing "with nil"
      (is (nil? ((hmac-gost nil) {} nil)))
      (is (nil? ((hmac-gost :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((hmac-gost (long 0)) {} nil))))
    (testing "with string"
      (is (= "df89c2a9e8ae6622d38813ee61d8202d6f424d7169a1eb73d0590d4ad6269625" ((hmac-gost "hello world" "secret") {} nil))))))

(deftest ripemd128-test
  (let [ripemd128 (partial function-transform :ripemd128)]
    (testing "with nil"
      (is (nil? ((ripemd128 nil) {} nil)))
      (is (nil? ((ripemd128 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((ripemd128 (long 0)) {} nil))))
    (testing "with string"
      (is (= "c52ac4d06245286b33953957be6c6f81" ((ripemd128 "hello world") {} nil))))))

(deftest hmac-ripemd128-test
  (let [hmac-ripemd128 (partial function-transform :hmac_ripemd128)]
    (testing "with nil"
      (is (nil? ((hmac-ripemd128 nil) {} nil)))
      (is (nil? ((hmac-ripemd128 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((hmac-ripemd128 (long 0)) {} nil))))
    (testing "with string"
      (is (= "692ec2f0d7c7d0649dc5de420c251bc7" ((hmac-ripemd128 "hello world" "secret") {} nil))))))

(deftest ripemd256-test
  (let [ripemd256 (partial function-transform :ripemd256)]
    (testing "with nil"
      (is (nil? ((ripemd256 nil) {} nil)))
      (is (nil? ((ripemd256 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((ripemd256 (long 0)) {} nil))))
    (testing "with string"
      (is (= "0d375cf9d9ee95a3bb15f757c81e93bb0ad963edf69dc4d12264031814608e37" ((ripemd256 "hello world") {} nil))))))

(deftest hmac-ripemd256-test
  (let [hmac-ripemd256 (partial function-transform :hmac_ripemd256)]
    (testing "with nil"
      (is (nil? ((hmac-ripemd256 nil) {} nil)))
      (is (nil? ((hmac-ripemd256 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((hmac-ripemd256 (long 0)) {} nil))))
    (testing "with string"
      (is (= "77d19c3c9aa57a4f17f82501aa72cdf469fc908499cdd90999b221d34354330a" ((hmac-ripemd256 "hello world" "secret") {} nil))))))

(deftest ripemd320-test
  (let [ripemd320 (partial function-transform :ripemd320)]
    (testing "with nil"
      (is (nil? ((ripemd320 nil) {} nil)))
      (is (nil? ((ripemd320 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((ripemd320 (long 0)) {} nil))))
    (testing "with string"
      (is (= "0e12fe7d075f8e319e07c106917eddb0135e9a10aefb50a8a07ccb0582ff1fa27b95ed5af57fd5c6" ((ripemd320 "hello world") {} nil))))))

(deftest hmac-ripemd320-test
  (let [hmac-ripemd320 (partial function-transform :hmac_ripemd320)]
    (testing "with nil"
      (is (nil? ((hmac-ripemd320 nil) {} nil)))
      (is (nil? ((hmac-ripemd320 :col1) {:col1 nil} nil))))
    (testing "with number"
      (is (thrown? Exception ((hmac-ripemd320 (long 0)) {} nil))))
    (testing "with string"
      (is (= "edabe5f8fb842df66d677861c8564bf8671be9bd1ab14028ecf75c915255b31c77c43cdde5b94524" ((hmac-ripemd320 "hello world" "secret") {} nil))))))

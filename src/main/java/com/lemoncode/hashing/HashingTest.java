package com.lemoncode.hashing;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.zip.CRC32;

public class HashingTest {
    private final Hash hash;
    private final int times;

    public HashingTest(Hash hash, int times) {
        this.hash = hash;
        this.times = times;
    }

    public enum Hash {
        SHA1, MD5, SHA256, SHA512, MURMUR3, CRC32, XXH3, FARM, ADLER32
    }

    public static void main(String[] args) throws Exception {
        Hash hash = Hash.valueOf(args[0]);
        int times = Integer.parseInt(args[1]);
        HashingTest hashingTest = new HashingTest(hash, times);
        hashingTest.run();
    }

    public void run() {
        String payload = duplicate(this.times);
        System.out.println("Payload length: " + payload.length() / 1000 + " kb");
        System.out.println("Num Fields: " + (29 * this.times));
        Instant start = Instant.now();
        String hash = hashIt(payload);
        System.out.println(hash);
        System.out.println("Duration: " + (Duration.between(start, Instant.now()).toMillis()));

    }

    private String hashIt(String payload) {
        switch (this.hash) {
            case MD5:
                return DigestUtils.md5Hex(payload);
            case SHA1:
                return DigestUtils.sha1Hex(payload);
            case SHA256:
                return DigestUtils.sha256Hex(payload);
            case SHA512:
                return DigestUtils.sha512Hex(payload);
            case MURMUR3:
                return Hashing.murmur3_128().newHasher().putString(payload, StandardCharsets.UTF_8).hash().toString();
            case FARM:
                return Hashing.farmHashFingerprint64().newHasher().putString(payload, StandardCharsets.UTF_8).hash().toString();
            case CRC32:
                CRC32 fileCRC32 = new CRC32();
                fileCRC32.update(payload.getBytes(StandardCharsets.UTF_8));
                return Long.toHexString(fileCRC32.getValue());
            case ADLER32:
                return Hashing.adler32().newHasher().putString(payload, StandardCharsets.UTF_8).hash().toString();
            default:
                throw new IllegalArgumentException("Not a known hsah");
        }
    }

    String head = "{\n" +
            "   \"logicalObject\": \"JournalSource\",\n" +
            "   \"operationType\": \"U\",\n" +
            "   \"payload\": {\n";

    String fields = " \"$sourceLang\": \"en\",\n" +
            "      \"attribute1\": \"test3\",\n" +
            "      \"attribute2\": 112,\n" +
            "      \"attribute3\": 1111,\n" +
            "      \"attribute4\": 'teststata12',\n" +
            "      \"attribute5\": 'a longer kind of test',\n" +
            "      \"attributeCategory\": null,\n" +
            "      \"attributeDate1\": '121231231\",\n" +
            "      \"attributeDate2\": \"abcd efadfa ha\",\n" +
            "      \"attributeDate3\": \"flink outbound aab\",\n" +
            "      \"attributeDate4\": \"1212312\",\n" +
            "      \"attributeDate5\": \"PHP\",\n" +
            "      \"attributeNumber1\": \"MBTC\",\n" +
            "      \"attributeNumber2\": \"heloo world....\",\n" +
            "      \"attributeNumber3\": \"sda   adfasd        adfada\",\n" +
            "      \"attributeNumber4\": \"safa\",\n" +
            "      \"attributeNumber5\": zxczczcvzcvzdsadfad adsfa,\n" +
            "      \"createdBy\": \"0\",\n" +
            "      \"description\": \"Revaluation journal entry.\",\n" +
            "      \"effectiveDateRuleCode\": \"R\",\n" +
            "      \"importUsingKeyFlag\": \"N\",\n" +
            "      \"jeSourceName\": \"FLINK-1-Revaluation\",\n" +
            "      \"journalApprovalFlag\": \"N\",\n" +
            "      \"journalReferenceFlag\": \"N\",\n" +
            "      \"jeSourceKey\": \"FLINK-1-Revaluation\",\n" +
            "      \"updatedBy\": \"SEED_DATA_FROM_APPLICATION\",\n" +
            "      \"overrideEditsFlag\": \"N\",\n" +
            "      \"seedDataSource\": \"fin/gl/db/data/FinGlJrnlSetupSources/JournalSourceSD.xml\",\n" +
            "      \"singleCurrencyJournalFlag\": null,";
    String tail = "      }" +
            "}";


    private String duplicate(int times) {
        StringBuilder sb = new StringBuilder(head);
        sb.append(String.valueOf(fields).repeat(Math.max(0, times)));
        sb.append(tail);
        return sb.toString();
    }

}

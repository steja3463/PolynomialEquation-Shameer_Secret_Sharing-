import java.io.*;
import java.math.BigInteger;
import java.util.*;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) throws Exception {
        String fileName = "input1.json"; // change to input2.json for 2nd test case
        // Read JSON file
        StringBuilder jsonContent = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            jsonContent.append(line);
        }
        br.close();

        JSONObject json = new JSONObject(jsonContent.toString());
        JSONObject keys = json.getJSONObject("keys");
        int k = keys.getInt("k");

        List<BigInteger> xList = new ArrayList<>();
        List<BigInteger> yList = new ArrayList<>();

        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;
            int x = Integer.parseInt(key);
            JSONObject pair = json.getJSONObject(key);
            int base = Integer.parseInt(pair.getString("base"));
            String value = pair.getString("value");

            BigInteger y = new BigInteger(value, base);
            xList.add(BigInteger.valueOf(x));
            yList.add(y);

            if (xList.size() == k) break;
        }

        BigInteger secret = lagrangeInterpolation(xList, yList);
        System.out.println("Secret (c) value: " + secret);
    }

    // Lagrange Interpolation at x = 0 gives constant term c
    private static BigInteger lagrangeInterpolation(List<BigInteger> x, List<BigInteger> y) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < x.size(); i++) {
            BigInteger xi = x.get(i);
            BigInteger yi = y.get(i);

            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < x.size(); j++) {
                if (i == j) continue;
                BigInteger xj = x.get(j);

                numerator = numerator.multiply(xj.negate());
                denominator = denominator.multiply(xi.subtract(xj));
            }

            BigInteger term = yi.multiply(numerator).divide(denominator);
            result = result.add(term);
        }

        return result;
    }
}

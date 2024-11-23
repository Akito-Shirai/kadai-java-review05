import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Review05 {

    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;


        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            // DB接続
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/kadaidb?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "パスワード"
                    );

            // DB接続確認
            if (con == null || con.isClosed()) {
                System.out.println("DBの接続ができていないか、接続か切れています。");
                return;
            }

            // PreparedStatementオブジェクトの作成
            String selectSql = "SELECT * FROM person WHERE id = ?";
            stmt = con.prepareStatement(selectSql);

            System.out.println("検索キーワードを入力してください > ");
            int userInputNum = keyInNum();

            // 実行
            stmt.setInt(1, userInputNum);
            rs = stmt.executeQuery();

            // 結果表示
            if (!rs.next()) {
                System.out.println("No results found for id: " + userInputNum);
                System.out.println("データ有無、またはDB接続の権限設定を確認してください。");
            } else {
                do {
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    System.out.println("<Result>");
                    System.out.println(name + "\n" + age);
                } while (rs.next());
            }

        } catch (ClassNotFoundException e) {
            System.err.println("JDBCドライバのロードに失敗しました。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("データベースに異常が発生しました。");
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("ResultSetを閉じる際にエラーが発生しました。");
                    e.printStackTrace();
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("PreparedStatementを閉じる際にエラーが発生しました。");
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("DB切断時にエラーが発生しました。");
                    e.printStackTrace();
                }
            }
        }
    }

    public static String keyIn() {
        String line = null;
        try {
            BufferedReader key = new BufferedReader(new InputStreamReader(System.in));
            line = key.readLine();
        } catch(IOException e) {
        }
        return line;
    }

    public static int keyInNum() {
        int result = 0;
        while(true) {
            try {
                result = Integer.parseInt(keyIn());
                break;
            } catch (NumberFormatException e) {
                System.out.println("数値を入力してください。");
            }
        }
        return result;
    }

}

public class Main {
    public static void main(String[] args) {
        Screen game = new Screen();
        // 在代码中添加这行来查看当前工作目录
        System.out.println("当前工作目录: " + System.getProperty("user.dir"));
        game.setVisible(true);
    }
}

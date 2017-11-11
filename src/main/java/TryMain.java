public class TryMain {

    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TreeConfigurator();
            }
        });
    }
}

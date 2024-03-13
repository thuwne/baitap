import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class baitapclock {
    private JFrame frame;
    private JLabel clockLabel;
    private JTextField timezoneTextField;
    private Thread updateTimeThread;

    public baitapclock() {
        frame = new JFrame("Clock");
        frame.setSize(471, 312);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(255, 192, 203));
        controlPanel.setBounds(0, 119, 457, 146);
        timezoneTextField = new JTextField(10);
        timezoneTextField.setBounds(163, 113, 116, 40);
        JButton updateButton = new JButton("OPEN");
        updateButton.setBackground(new Color(240, 230, 140));
        updateButton.setBounds(302, 109, 91, 42);
        updateButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        updateButton.addActionListener(e -> updateClock());
        controlPanel.setLayout(null);

        JLabel label = new JLabel("Timezone: ");
        label.setBounds(76, 111, 77, 40);
        label.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 16));
        controlPanel.add(label);
        controlPanel.add(timezoneTextField);
        controlPanel.add(updateButton);

        frame.getContentPane().add(controlPanel);

        clockLabel = new JLabel();
        clockLabel.setBounds(0, 20, 457, 99);
        controlPanel.add(clockLabel);
        clockLabel.setForeground(new Color(0, 0, 255));
        clockLabel.setBackground(new Color(255, 240, 245));
        clockLabel.setFont(new Font("Tahoma", Font.PLAIN, 23));
        clockLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.setVisible(true);

        startClockThread();
    }
    private void startClockThread() {
        updateTimeThread = new Thread(() -> {
            while (true) {
                updateClockLabel();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        updateTimeThread.start();
    }

    private void updateClockLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        clockLabel.setText(sdf.format(new Date(System.currentTimeMillis() + timeZone.getRawOffset())));
    }

    private void updateClock() {
        String timezoneText = timezoneTextField.getText().trim();
        if (!timezoneText.isEmpty()) {
            String enteredTime = timezoneText;

            TimeZone timeZone = TimeZone.getTimeZone(timezoneText);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sdf.setTimeZone(timeZone);

            Date currentTime;
            try {
                currentTime = sdf.parse(enteredTime);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }

            JFrame newClockFrame = new JFrame("New Clock");
            JLabel newClockLabel = new JLabel(sdf.format(currentTime));
            newClockLabel.setHorizontalAlignment(JLabel.CENTER);
            newClockFrame.getContentPane().add(newClockLabel);

            newClockFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newClockFrame.setSize(300, 150);
            newClockFrame.setVisible(true);

            Thread updateTimeThread = new Thread(() -> {
                while (true) {
                    SwingUtilities.invokeLater(() -> {
                        currentTime.setTime(currentTime.getTime() + 1000);
                        newClockLabel.setText(sdf.format(currentTime));
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            updateTimeThread.start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(baitapclock::new);
    }
}

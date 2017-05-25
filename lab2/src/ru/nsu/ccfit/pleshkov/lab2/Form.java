package ru.nsu.ccfit.pleshkov.lab2;

import ru.nsu.ccfit.pleshkov.lab2.factory.FormStartObjects;
import ru.nsu.ccfit.pleshkov.lab2.factory.Initializer;
import ru.nsu.ccfit.pleshkov.lab2.factory.Observable;
import ru.nsu.ccfit.pleshkov.lab2.factory.Observer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Form extends JFrame {
    private JPanel Panel;
    private JLabel numberOfBodies;
    private JLabel numberOfEngines;
    private JLabel numberOfAccessories;
    private JLabel numberOfProfit;
    private JLabel numberOfCars;
    private JLabel textEngines;
    private JLabel textBodies;
    private JLabel textAccessories;
    private JLabel textProfit;
    private JTextField enginesSleepTimeNumber;
    private JSlider enginesSlider;
    private JLabel enginesSleepTimeText;
    private JTextField accessoriesSleepTimeNumber;
    private JSlider accessoriesSlider;
    private JLabel accessoriesSleepTimeText;
    private JLabel bodiesSleepTimeText;
    private JTextField bodiesSleepTimeNumber;
    private JSlider bodiesSlider;
    private JTextField dealersSleepTimeNumber;
    private JSlider dealersSlider;
    private JCheckBox checkBox;
    private JLabel dealersSleepTimeText;
    private JLabel textCars;
    private JLabel textWorkers;
    private JLabel numberOfWorkers;

    private TextAndSlider accessoriesSleep;
    private TextAndSlider bodiesSleep;
    private TextAndSlider enginesSleep;
    private TextAndSlider dealersSleep;
    private LogBox logBox;

    private static String labelDelimiter = " / ";

    private String numberOfDealersString;
    private String totalNumberOfWorkersString;
    private String accessoriesStorageCapacityString;
    private String bodiesStorageCapacityString;
    private String enginesStorageCapacityString;
    private String carStorageCapacityString;

    private static final String numberOfEnginesString = "Number of engines:";
    private static final String numberOfBodiesString = "Number of bodies:";
    private static final String numberOfAccessoriesString = "Number of accessories:";
    private static final String numberOfCarsString = "Number of cars:";
    private static final String numberOfWorkersString = "Number of waiting workers:";
    private static final String profitString = "PROFIT:";
    private static final String enginesSleepTimeString = "Engines Sleep Time";
    private static final String accessoriesSleepTimeString = "Accessories Sleep Time";
    private static final String bodiesSleepTimeString = "Bodies Sleep Time";
    private static final String dealersSleepTimeString = "Dealers Sleep Time";

    public Form(FormStartObjects startObjects) {
        numberOfDealersString = " by " + startObjects.getNumberOfDealers() + " dealers";
        accessoriesStorageCapacityString = labelDelimiter + String.valueOf(startObjects.getAccessoriesStorageCapacity())
                + " by " + String.valueOf(startObjects.getNumberOfAccessoriesSuppliers()) + " suppliers";
        bodiesStorageCapacityString = labelDelimiter + String.valueOf(startObjects.getBodiesStorageCapacity());
        enginesStorageCapacityString = labelDelimiter + String.valueOf(startObjects.getEnginesStorageCapacity());
        carStorageCapacityString = labelDelimiter + String.valueOf(startObjects.getCarStorageCapacity());
        totalNumberOfWorkersString = labelDelimiter + String.valueOf(startObjects.getNumberOfWorkers());
        setBounds(400, 200, 600, 500);
        setContentPane(Panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textEngines.setText(numberOfEnginesString);
        textBodies.setText(numberOfBodiesString);
        textAccessories.setText(numberOfAccessoriesString);
        textProfit.setText(profitString);
        textCars.setText(numberOfCarsString);
        textWorkers.setText(numberOfWorkersString);
        enginesSleepTimeText.setText(enginesSleepTimeString);
        accessoriesSleepTimeText.setText(accessoriesSleepTimeString);
        bodiesSleepTimeText.setText(bodiesSleepTimeString);
        dealersSleepTimeText.setText(dealersSleepTimeString);
        bodiesSleep = new TextAndSlider(bodiesSleepTimeNumber, bodiesSlider);
        bodiesSleep.addObserver(startObjects.getBodiesSleepObserver());
        enginesSleep = new TextAndSlider(enginesSleepTimeNumber, enginesSlider);
        enginesSleep.addObserver(startObjects.getEnginesSleepObserver());
        accessoriesSleep = new TextAndSlider(accessoriesSleepTimeNumber, accessoriesSlider);
        accessoriesSleep.addObserver(startObjects.getAccessoriesSleepObserver());
        dealersSleep = new TextAndSlider(dealersSleepTimeNumber, dealersSlider);
        dealersSleep.addObserver(startObjects.getDealersSleepObserver());
        logBox = new LogBox(checkBox, startObjects.isToLog());
        logBox.addObserver(startObjects.getLoggingObserver());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Initializer.finish();
                Form.this.dispose();
            }
        });
        setVisible(true);
    }

    public void updateNumberOfBodies(int newData) {
        numberOfBodies.setText(String.valueOf(newData) + bodiesStorageCapacityString);
    }

    public void updateNumberOfAccessories(int newData) {
        numberOfAccessories.setText(String.valueOf(newData) + accessoriesStorageCapacityString);
    }

    public void updateNumberOfEngines(int newData) {
        numberOfEngines.setText(String.valueOf(newData) + enginesStorageCapacityString);
    }

    public void updateNumberOfCars(int newData) {
        numberOfCars.setText(String.valueOf(newData) + carStorageCapacityString);
    }

    public void updateNumberOfWorkers(int newData) {
        numberOfWorkers.setText(String.valueOf(newData) + totalNumberOfWorkersString);
    }

    public void updateProfit(int newData) {
        numberOfProfit.setText(String.valueOf(newData) + numberOfDealersString);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        Panel = new JPanel();
        Panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(10, 5, new Insets(20, 0, 0, 0), 0, 0));
        Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Factory"));
        numberOfBodies = new JLabel();
        numberOfBodies.setText("Label");
        Panel.add(numberOfBodies, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberOfEngines = new JLabel();
        numberOfEngines.setText("Label");
        Panel.add(numberOfEngines, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberOfAccessories = new JLabel();
        numberOfAccessories.setText("Label");
        Panel.add(numberOfAccessories, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enginesSleepTimeText = new JLabel();
        enginesSleepTimeText.setText("Label");
        Panel.add(enginesSleepTimeText, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enginesSleepTimeNumber = new JTextField();
        Panel.add(enginesSleepTimeNumber, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        enginesSlider = new JSlider();
        Panel.add(enginesSlider, new com.intellij.uiDesigner.core.GridConstraints(6, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accessoriesSleepTimeText = new JLabel();
        accessoriesSleepTimeText.setText("Label");
        Panel.add(accessoriesSleepTimeText, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accessoriesSleepTimeNumber = new JTextField();
        Panel.add(accessoriesSleepTimeNumber, new com.intellij.uiDesigner.core.GridConstraints(7, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        accessoriesSlider = new JSlider();
        Panel.add(accessoriesSlider, new com.intellij.uiDesigner.core.GridConstraints(7, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bodiesSleepTimeText = new JLabel();
        bodiesSleepTimeText.setText("Label");
        Panel.add(bodiesSleepTimeText, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bodiesSleepTimeNumber = new JTextField();
        Panel.add(bodiesSleepTimeNumber, new com.intellij.uiDesigner.core.GridConstraints(8, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        bodiesSlider = new JSlider();
        Panel.add(bodiesSlider, new com.intellij.uiDesigner.core.GridConstraints(8, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textBodies = new JLabel();
        textBodies.setText("Label");
        Panel.add(textBodies, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textEngines = new JLabel();
        textEngines.setText("Label");
        Panel.add(textEngines, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textAccessories = new JLabel();
        textAccessories.setText("Label");
        Panel.add(textAccessories, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        Panel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        Panel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(6, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        checkBox = new JCheckBox();
        checkBox.setText("CheckBox");
        Panel.add(checkBox, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dealersSleepTimeText = new JLabel();
        dealersSleepTimeText.setText("Label");
        Panel.add(dealersSleepTimeText, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dealersSleepTimeNumber = new JTextField();
        dealersSleepTimeNumber.setText("");
        Panel.add(dealersSleepTimeNumber, new com.intellij.uiDesigner.core.GridConstraints(9, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        dealersSlider = new JSlider();
        Panel.add(dealersSlider, new com.intellij.uiDesigner.core.GridConstraints(9, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textCars = new JLabel();
        textCars.setText("Label");
        Panel.add(textCars, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textProfit = new JLabel();
        textProfit.setText("Label");
        Panel.add(textProfit, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberOfCars = new JLabel();
        numberOfCars.setText("Label");
        Panel.add(numberOfCars, new com.intellij.uiDesigner.core.GridConstraints(3, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberOfProfit = new JLabel();
        numberOfProfit.setText("Label");
        Panel.add(numberOfProfit, new com.intellij.uiDesigner.core.GridConstraints(5, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textWorkers = new JLabel();
        textWorkers.setText("Label");
        Panel.add(textWorkers, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberOfWorkers = new JLabel();
        numberOfWorkers.setText("Label");
        Panel.add(numberOfWorkers, new com.intellij.uiDesigner.core.GridConstraints(4, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return Panel;
    }

    private class LogBox implements Observable {
        private JCheckBox box;

        private ArrayList<Observer> observers = new ArrayList<>();

        @Override
        public void removeObserver(Observer observer) {
            if (observers.contains(observer)) {
                observers.remove(observer);
            }
        }

        @Override
        public void addObserver(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void notifyObservers() {
            for (Observer observer : observers) {
                if (observer != null) {
                    observer.update(0);
                }
            }
        }

        LogBox(JCheckBox box, boolean toLog) {
            this.box = box;
            box.setText("Logging");
            box.setSelected(toLog);
            box.addActionListener((ActionEvent e) -> notifyObservers());
        }
    }

    private class TextAndSlider implements Observable {
        private JTextField text;
        private JSlider slider;

        private int sleepTime;
        private int maximumValue;

        private ArrayList<Observer> observers = new ArrayList<>();


        @Override
        public void removeObserver(Observer observer) {
            if (observers.contains(observer)) {
                observers.remove(observer);
            }
        }

        @Override
        public void addObserver(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void notifyObservers() {
            for (Observer observer : observers) {
                if (observer != null) {
                    observer.update(sleepTime);
                }
            }
        }

        TextAndSlider(JTextField text, JSlider slider) {
            this.text = text;
            this.slider = slider;
            this.maximumValue = Initializer.MAX_SLEEP_TIME;
            slider.setMaximum(maximumValue);
            slider.setMinimum(0);
            slider.setValue(Initializer.INIT_SLEEP_TIME);
            text.setText(String.valueOf(Initializer.INIT_SLEEP_TIME));
            text.addActionListener((ActionEvent e) -> {
                String sleep = text.getText();
                if (sleep != null && !sleep.isEmpty()) {
                    try {
                        int sl = Integer.parseInt(sleep);
                        if (sl < 0 || sl > maximumValue) {
                            SwingUtilities.invokeLater(() -> text.setForeground(Color.RED));
                            return;
                        }
                        if (text.getForeground() == Color.RED) {
                            SwingUtilities.invokeLater(() -> text.setForeground(Color.BLACK));
                        }
                        slider.setValue(sl);
                        sleepTime = sl;
                        notifyObservers();
                    } catch (NumberFormatException ex) {
                        SwingUtilities.invokeLater(() -> text.setForeground(Color.RED));
                    }
                }
            });
            slider.addChangeListener((ChangeEvent e) -> {
                sleepTime = slider.getValue();
                if (text.getForeground() == Color.RED) {
                    SwingUtilities.invokeLater(() -> text.setForeground(Color.BLACK));
                }
                SwingUtilities.invokeLater(() -> text.setText(String.valueOf(sleepTime)));
                notifyObservers();
            });
        }
    }

    private void createUIComponents() {

    }

}

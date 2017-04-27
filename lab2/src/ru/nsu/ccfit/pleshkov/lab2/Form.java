package ru.nsu.ccfit.pleshkov.lab2;

import ru.nsu.ccfit.pleshkov.lab2.controller.Controller;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class Form extends JFrame {
    private JPanel Panel;
    private JLabel numberOfBodies;
    private JLabel numberOfEngines;
    private JLabel textEngines;
    private JLabel numberOfAccessories;
    private JLabel textBodies;
    private JLabel textAccessories;
    private JLabel textProfit;
    private JLabel numberOfProfit;
    private JTextField enginesSleepTimeNumber;
    private JSlider enginesSlider;
    private JLabel enginesSleepTimeText;
    private JTextField accessoriesSleepTimeNumber;
    private JSlider accessoriesSlider;
    private JLabel accessoriesSleepTimeText;
    private JLabel bodiesSleepTimeText;
    private JTextField bodiesSleepTimeNumber;
    private JSlider bodiesSlider;

    private String labelDelimiter = " / ";

    public void setNumberOfDealers(int numberOfDealers) {
        this.numberOfDealers = numberOfDealers;
        numberOfDealersString = " by " + numberOfDealers + " dealers";
    }

    private int numberOfDealers;
    private String numberOfDealersString;


    public void setNumberOfAccessoriesSuppliers(int numberOfAccessoriesSuppliers) {
        this.numberOfAccessoriesSuppliers = numberOfAccessoriesSuppliers;
        accessoriesStorageCapacityString = labelDelimiter + String.valueOf(accessoriesStorageCapacity)
                + " by " + String.valueOf(numberOfAccessoriesSuppliers) + " suppliers";
    }

    private int numberOfAccessoriesSuppliers;
    private String numberOfAccessoriesSuppliersString;

    public void setAccessoriesStorageCapacity(int accessoriesStorageCapacity) {
        this.accessoriesStorageCapacity = accessoriesStorageCapacity;
        accessoriesStorageCapacityString = labelDelimiter + String.valueOf(accessoriesStorageCapacity)
                + " by " + String.valueOf(numberOfAccessoriesSuppliers) + " suppliers";
    }

    private int accessoriesStorageCapacity;
    private String accessoriesStorageCapacityString;

    public void setBodiesStorageCapacity(int bodiesStorageCapacity) {
        this.bodiesStorageCapacity = bodiesStorageCapacity;
        bodiesStorageCapacityString = labelDelimiter + String.valueOf(bodiesStorageCapacity);
    }

    private int bodiesStorageCapacity;
    private String bodiesStorageCapacityString;

    public void setEnginesStorageCapacity(int enginesStorageCapacity) {
        this.enginesStorageCapacity = enginesStorageCapacity;
        enginesStorageCapacityString = labelDelimiter + String.valueOf(enginesStorageCapacity);
    }

    private int enginesStorageCapacity;
    private String enginesStorageCapacityString;

    private Controller controller;

    public Form() {
        setBounds(400, 200, 600, 500);
        setContentPane(Panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textEngines.setText("Number of engines:");
        textBodies.setText("Number of bodies:");
        textAccessories.setText("Number of accessories:");
        textProfit.setText("PROFIT:");
        enginesSleepTimeText.setText("Engines Sleep Time");
        enginesSlider.setMaximum(Controller.MAX_SLEEP_TIME);
        enginesSlider.setMinimum(0);
        accessoriesSleepTimeText.setText("Accessories Sleep Time");
        accessoriesSlider.setMaximum(Controller.MAX_SLEEP_TIME);
        accessoriesSlider.setMinimum(0);
        bodiesSleepTimeText.setText("Bodies Sleep Time");
        bodiesSlider.setMaximum(Controller.MAX_SLEEP_TIME);
        bodiesSlider.setMinimum(0);
        enginesSleepTimeNumber.setPreferredSize(new Dimension(5, 5));
        enginesSleepTimeNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sleep = enginesSleepTimeNumber.getText();
                if (sleep != null && !sleep.isEmpty()) {
                    System.out.println(sleep);
                    int sl = -1;
                    try {
                        sl = Integer.parseInt(sleep);
                    } catch (NumberFormatException ex) {

                    }
                    if ((sl >= 0) && (sl <= Controller.MAX_SLEEP_TIME)) {
                        setEnginesSlider(sl);
                    }
                    controller.changeEnginesSleepTime(sl);
                }
            }
        });
        enginesSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int sleep = enginesSlider.getValue();
                if ((sleep >= 0) && (sleep <= Controller.MAX_SLEEP_TIME)) {
                    setEnginesSleepTimeNumber(sleep);
                }

                controller.changeEnginesSleepTime(sleep);
            }

        });

        accessoriesSleepTimeNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sleep = accessoriesSleepTimeNumber.getText();
                if (sleep != null && !sleep.isEmpty()) {
                    System.out.println(sleep);
                    int sl = -1;
                    try {
                        sl = Integer.parseInt(sleep);
                    } catch (NumberFormatException ex) {

                    }
                    if ((sl >= 0) && (sl <= Controller.MAX_SLEEP_TIME)) {
                        setAccessoriesSlider(sl);
                    }
                    controller.changeAccessoriesSleepTime(sl);
                }
            }
        });
        accessoriesSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int sleep = accessoriesSlider.getValue();
                if ((sleep >= 0) && (sleep <= Controller.MAX_SLEEP_TIME)) {
                    setAccessoriesSleepTimeNumber(sleep);
                }
                controller.changeAccessoriesSleepTime(sleep);
            }

        });

        bodiesSleepTimeNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sleep = bodiesSleepTimeNumber.getText();
                if (sleep != null && !sleep.isEmpty()) {
                    System.out.println(sleep);
                    int sl = -1;
                    try {
                        sl = Integer.parseInt(sleep);
                    } catch (NumberFormatException ex) {

                    }
                    if ((sl >= 0) && (sl <= Controller.MAX_SLEEP_TIME)) {
                        setBodiesSlider(sl);
                    }
                    controller.changeBodiesSleepTime(sl);
                }
            }
        });
        bodiesSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                int sleep = bodiesSlider.getValue();
                if ((sleep >= 0) && (sleep <= Controller.MAX_SLEEP_TIME)) {
                    setBodiesSleepTimeNumber(sleep);
                }
                controller.changeBodiesSleepTime(sleep);
            }

        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.finish();
                Form.this.dispose();
            }
        });
        setVisible(true);
    }

    public void setEnginesSleep(int sleep) {
        setEnginesSleepTimeNumber(sleep);
        setEnginesSlider(sleep);
    }

    private void setEnginesSleepTimeNumber(int sleep) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                enginesSleepTimeNumber.setText(String.valueOf(sleep));
            }
        });
    }

    private void setEnginesSlider(int sleep) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                enginesSlider.setValue(sleep);
            }
        });
    }

    public void setAccessoriesSleep(int sleep) {
        setAccessoriesSleepTimeNumber(sleep);
        setAccessoriesSlider(sleep);
    }

    private void setAccessoriesSleepTimeNumber(int sleep) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                accessoriesSleepTimeNumber.setText(String.valueOf(sleep));
            }
        });
    }

    private void setAccessoriesSlider(int sleep) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                accessoriesSlider.setValue(sleep);
            }
        });
    }

    public void setBodiesSleep(int sleep) {
        setBodiesSleepTimeNumber(sleep);
        setBodiesSlider(sleep);
    }

    private void setBodiesSleepTimeNumber(int sleep) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bodiesSleepTimeNumber.setText(String.valueOf(sleep));
            }
        });
    }

    private void setBodiesSlider(int sleep) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bodiesSlider.setValue(sleep);
            }
        });
    }

    public void setController(Controller controller) {
        this.controller = controller;

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

    public void updateProfit(int newData) {
        numberOfProfit.setText(String.valueOf(newData) + numberOfDealersString);
    }

}

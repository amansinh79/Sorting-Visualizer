package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class frame {
    //width of panel used in array
    private static int w;
    //height of panel used in array
    private static int h;
    //array to sort
    private static int[] arr;
    private static JPanel panel;
    private static JPanel buttonPanel;
    private static Random random;
    private static Set<Integer> currentlySorting;
    private static Set<Integer> sorted;
    private static long delay;


    // initializing stuff
    frame() {

        random = new Random();
        Frame f = new JFrame();
        delay = 1;
        f.setTitle("Sorting Visualizer");
        panel = new JPanel(new GridLayout());
        buttonPanel = new JPanel(new FlowLayout());
        f.setSize(1000, 500);

        panel.setBounds(0, 0, f.getWidth(), 400);

        buttonPanel.setBounds(0, 400, f.getWidth(), f.getHeight() - 400);
        buttonPanel.setBackground(Color.white);
        f.add(buttonPanel);

        h = panel.getHeight()- 20;
        w = 140 ;
        panel.setBackground(Color.black);

        arr = new int[w];
        currentlySorting = new HashSet<>();
        sorted = new HashSet<>();

        setArr();

        panel.add(new MyCanvas());

        f.add(panel);
        f.setLayout(null);
        f.setResizable(false);

        JButton cocktailButton = new JButton();
        cocktailButton.setPreferredSize(new Dimension(120, 30));
        cocktailButton.setText("Cocktail Sort");
        buttonPanel.add(cocktailButton);

        JButton bubbleButton = new JButton();
        bubbleButton.setPreferredSize(new Dimension(120, 30));
        bubbleButton.setText("Bubble Sort");
        buttonPanel.add(bubbleButton);

        JButton selectionButton = new JButton();
        selectionButton.setPreferredSize(new Dimension(120, 30));
        selectionButton.setText("Selection Sort");
        buttonPanel.add(selectionButton);

        JButton mergeButton = new JButton();
        mergeButton.setPreferredSize(new Dimension(120, 30));
        mergeButton.setText("Merge Sort");
        buttonPanel.add(mergeButton);

        JButton shuffle = new JButton();
        shuffle.setPreferredSize(new Dimension(100, 30));
        shuffle.setText("Shuffle");
        buttonPanel.add(shuffle);

        JSlider arraySizeSlider = new JSlider(JSlider.HORIZONTAL, 1, w, w);
        arraySizeSlider.setPreferredSize(new Dimension(150,30));
        arraySizeSlider.setToolTipText("Array Size");
        buttonPanel.add(arraySizeSlider);

        JSlider delaySlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
        delaySlider.setPreferredSize(new Dimension(140,30));
        delaySlider.setToolTipText("Delay");
        buttonPanel.add(delaySlider);

        arraySizeSlider.addChangeListener(this::sizeChanged);
        delaySlider.addChangeListener(this::delay);
        shuffle.addActionListener(this::shuffle);

        cocktailButton.addActionListener(actionEvent -> {
            buttonPanel.setVisible(false);
            new Thread(Sort::cocktailSort).start();
        });
        bubbleButton.addActionListener(actionEvent -> {
            buttonPanel.setVisible(false);
            new Thread(Sort::bubbleSort).start();
        });

        selectionButton.addActionListener(actionEvent -> {
            buttonPanel.setVisible(false);
            new Thread(Sort::selectionSort).start();
        });

        mergeButton.addActionListener(e -> {
            buttonPanel.setVisible(false);
            new Thread(Sort::mergeSortCall).start();
        });

        f.setVisible(true);
    }
    //speed of sorting
    private void delay(ChangeEvent e) {
        JSlider j = (JSlider)e.getSource();
        delay = j.getValue();
    }

    private void sizeChanged(ChangeEvent e) {
        JSlider j = (JSlider)e.getSource();
        w = j.getValue();
        setArr();

    }

    private void setArr(){
        arr = new int[w];
        currentlySorting = new HashSet<>();
        sorted = new HashSet<>();
        for (int i = 0; i < w; i++) {
            int temp = random.nextInt(h);
            while (temp < 5)
                temp = random.nextInt(h);
            arr[i] = temp;
        }
        panel.repaint();
    }

    private void shuffle(ActionEvent e) {
        setArr();
    }

    // sort class
    private static class Sort extends Thread {

       static private void threadSleep(){
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        static private void cocktailSort() {
            int i = 0;
            int j = 0;
            while (i < w/2-1) {
                for (; j < w - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        currentlySorting.add(j);
                        currentlySorting.add(j+1);
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        threadSleep();
                        panel.repaint();
                    }
                    currentlySorting.remove(j);
                    currentlySorting.remove(j+1);
                }
                sorted.add(w - i - 1);
                j--;
                for (; j >= 0; j--) {
                    if (arr[j+1] < arr[j]) {
                        currentlySorting.add(j);
                        currentlySorting.add(j+1);
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        threadSleep();
                        panel.repaint();
                    }
                    currentlySorting.remove(j);
                    currentlySorting.remove(j+1);
                }
                sorted.add(i);
                j++;
                i++;
            }
            sorted.add(w/2-1);
            sorted.add(w/2);
            sorted.add(w/2+1 );
            SwingUtilities.updateComponentTreeUI(panel);
            buttonPanel.setVisible(true);
        }

        static private void bubbleSort() {
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < w - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        currentlySorting.add(j);
                        currentlySorting.add(j+1);
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        threadSleep();
                        panel.repaint();
                    }
                    currentlySorting.remove(j);
                    currentlySorting.remove(j+1);
                }
                sorted.add(w - i - 1);
            }
            buttonPanel.setVisible(true);
        }

        static private void selectionSort(){
            for(int i = 0; i < w ; i++){
                int min = arr[i];
                int minj = i;
                for(int j = i; j < w ; j++){
                    if(arr[j] < min){
                        min = arr[j];
                        minj = j;
                        panel.repaint();
                    }
                }
                currentlySorting.add(minj);
                panel.repaint();
                threadSleep();
                int temp = arr[i];
                arr[i] = arr[minj];
                arr[minj] = temp;
                currentlySorting.remove(minj);
                sorted.add(i);

                panel.repaint();
            }
            panel.repaint();
            buttonPanel.setVisible(true);
        }

        static private void mergeSortCall(){
           mergeSort(0,arr.length-1);
           currentlySorting.clear();
           for(int i = 0; i < w ; i++)
               sorted.add(i);
           panel.repaint();
           buttonPanel.setVisible(true);
        }

         private static void mergeSort(int left, int right) {
          if (left < right) {
              int mid = (left+right)/2;
              mergeSort(left, mid);
              mergeSort(mid+1, right);
              merge(left, mid, right);
              currentlySorting.add(right);
          }
     }

        static void merge(int left, int mid, int right) {

            int leftSize = mid - left + 1;
            int rightSize = right - mid;

            int[] leftPart = new int [leftSize];
            int[] rightPart = new int [rightSize];

            for (int i=0; i < leftSize; i++) {
                leftPart[i] = arr[left + i];
            }
            for (int j=0; j < rightSize; j++) {
                rightPart[j] = arr[mid + 1 + j];
            }

            int i = 0, j = 0;

            int k = left;
            while (i < leftSize && j < rightSize) {
                currentlySorting.add(k);
                if (leftPart[i] <= rightPart[j]) {
                    arr[k++] = leftPart[i++];
                } else {
                    arr[k++] = rightPart[j++];
                }
                panel.repaint();
                threadSleep();
            }

            while (i < leftSize) {
                arr[k++] = leftPart[i++];
                panel.repaint();
                threadSleep();
            }

            while (j < rightSize ) {
                arr[k++] = rightPart[j++];
                panel.repaint();
                threadSleep();
            }
        }
    }

    private static class MyCanvas extends JComponent {

        // paint method
        public void paint(Graphics g) {
            BasicStroke stroke = new BasicStroke(2);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(stroke);
            int rectWidth = 7;

            for(int i = w / 2 , j = panel.getWidth() / 2 ; i >= 0 && j >= 0 ; i-- , j-=7){
                //rectangles
                g2.setColor(colorSetter(i));
                g2.fillRect(j,panel.getHeight() - arr[i],rectWidth,arr[i]);
                //black outline
                g2.setColor(Color.black);
                g2.drawRect(j,panel.getHeight() - arr[i],rectWidth,arr[i]);
            }
            for(int i = w / 2 + 1 , j = panel.getWidth() / 2 +1; i < w && j < panel.getWidth() ; i++ , j+=7) {
                //rectangles
                g2.setColor(colorSetter(i));
                g2.fillRect(j, panel.getHeight() - arr[i], rectWidth, arr[i]);
                // black outline
                if (i != w / 2 + 1) {
                    g2.setColor(Color.black);
                    g2.drawRect(j, panel.getHeight() - arr[i], rectWidth, arr[i]);
                }
            }

        }
            //set color based on current state
        private static Color colorSetter(int i){
            if (currentlySorting.contains(i))
                return Color.ORANGE;
            else if (sorted.contains(i))
                return Color.GREEN;
            else
                return Color.white;
        }
    }
}
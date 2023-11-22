import procedures.AverageLowFilter;
import procedures.Brightness;
import procedures.Contrast;
import procedures.Dilation;
import procedures.Erosion;
import procedures.GaussLowFilter;
import procedures.HorizontalMirror;
import procedures.Kirsch;
import procedures.MedianLowFilter;
import procedures.ModeLowFilter;
import procedures.Rotate;
import procedures.Threshold;
import procedures.Translate;
import procedures.VerticalMirror;
import procedures.Zoom;
import utils.ChallengeFrame;
import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class CorelDraw {

    private JFrame frame;
    private BufferedImage originalImage;
    private BufferedImage transformedImage;
    private JLabel originalImageLabel;
    private JLabel transformedImageLabel;

    public CorelDraw() {
        frame = new JFrame("CorelDraw2.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());
        menuBar.add(createGeometricTransformMenu());
        menuBar.add(createFiltersMenu());
        menuBar.add(createMorphologyMenu());
        menuBar.add(createFeatureExtractionMenu());

        originalImageLabel = new JLabel("Imagem Original");
        transformedImageLabel = new JLabel("Imagem Transformada");

        JPanel imagePanel = new JPanel(new GridLayout(1, 2));
        imagePanel.add(originalImageLabel);
        imagePanel.add(transformedImageLabel);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(menuBar, BorderLayout.NORTH);
        frame.getContentPane().add(imagePanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JMenu createFileMenu () {
        JMenu fileMenu = new JMenu("Arquivo");
        JMenuItem openItem = new JMenuItem("Abrir imagem");
        JMenuItem saveItem = new JMenuItem("Salvar imagem");
        JMenuItem aboutItem = new JMenuItem("Sobre");
        JMenuItem exitItem = new JMenuItem("Sair");

        openItem.addActionListener(openFileListener);
        saveItem.addActionListener(saveFileListener);
        aboutItem.addActionListener(aboutListener);
        exitItem.addActionListener(exitListener);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(aboutItem);
        fileMenu.add(exitItem);

        return fileMenu;
    }

    public JMenu createGeometricTransformMenu () {
        JMenu geometricTransformMenu = new JMenu("Transformações Geométricas");
        JMenuItem translate = new JMenuItem("Transladar");
        JMenuItem rotate = new JMenuItem("Rotacionar");
        JMenuItem mirror = new JMenuItem("Espelhar");
        JMenuItem zoomIn = new JMenuItem("Aumentar");
        JMenuItem zoomOut = new JMenuItem("Diminuir");

        translate.addActionListener(translateImageListener);
        rotate.addActionListener(rotateImageListener);
        mirror.addActionListener(mirrorImageListener);
        zoomIn.addActionListener(zoomInListener);
        zoomOut.addActionListener(zoomOutListener);

        geometricTransformMenu.add(translate);
        geometricTransformMenu.add(rotate);
        geometricTransformMenu.add(mirror);
        geometricTransformMenu.add(zoomIn);
        geometricTransformMenu.add(zoomOut);

        return geometricTransformMenu;
    }

    public JMenu createFiltersMenu () {
        JMenu filtersMenu = new JMenu("Filtros");
        JMenuItem brightness = new JMenuItem("Brilho");
        JMenuItem contrast = new JMenuItem("Contraste");
        JMenuItem grayscale = new JMenuItem("Grayscale");
        JMenuItem lowPass = new JMenuItem("Passa Baixa");
        JMenuItem highPass = new JMenuItem("Passa Alta");
        JMenuItem threshold = new JMenuItem("Threshold");

        brightness.addActionListener(brightnessListener);
        contrast.addActionListener(contrastListener);
        grayscale.addActionListener(grayscaleListener);
        lowPass.addActionListener(lowPassListener);
        highPass.addActionListener(highPassListener);
        threshold.addActionListener(thresholdActionListener);

        filtersMenu.add(brightness);
        filtersMenu.add(contrast);
        filtersMenu.add(grayscale);
        filtersMenu.add(lowPass);
        filtersMenu.add(highPass);
        filtersMenu.add(threshold);

        return filtersMenu;
    }

    public JMenu createMorphologyMenu () {
        JMenu morphologyMenu = new JMenu("Morfologia Matemática");
        JMenuItem dilation = new JMenuItem("Dilatação");
        JMenuItem erosion = new JMenuItem("Erosão");
        JMenuItem opening = new JMenuItem("Abertura");
        JMenuItem closing = new JMenuItem("Fechamento");

        dilation.addActionListener(dilationListener);
        erosion.addActionListener(erosionListener);
        opening.addActionListener(openingListener);
        closing.addActionListener(closingListener);

        morphologyMenu.add(dilation);
        morphologyMenu.add(erosion);
        morphologyMenu.add(opening);
        morphologyMenu.add(closing);

        return morphologyMenu;
    }

    public JMenu createFeatureExtractionMenu () {
        JMenu featureExtractionMenu = new JMenu("Extração de Características");
        JMenuItem challengeItem = new JMenuItem("DESAFIO");

        challengeItem.addActionListener(challengeListener);

        featureExtractionMenu.add(challengeItem);

        return featureExtractionMenu;
    }

    ActionListener openFileListener = e -> {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                originalImage = ImageIO.read(selectedFile);
                transformedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                originalImageLabel.setIcon(new ImageIcon(originalImage));
                transformedImageLabel.setIcon(new ImageIcon(transformedImage));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    ActionListener saveFileListener = e -> {
        if (transformedImage != null) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    String fileName = selectedFile.getName().toLowerCase();
                    if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
                        fileName += ".png";
                    }

                    ImageIO.write(transformedImage, fileName.substring(fileName.lastIndexOf('.') + 1), selectedFile);

                    JOptionPane.showMessageDialog(frame, "Imagem salva com sucesso.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao salvar a imagem: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Nenhuma imagem transformada para salvar.");
        }
    };

    ActionListener aboutListener = e -> {
        JOptionPane.showMessageDialog(frame, "CorelDraw desenvolvido na cadeira de processamento digital de imagens por Davi Schneider.");
    };

    ActionListener exitListener = e -> {
        int choice = JOptionPane.showConfirmDialog(frame, "Tem certeza que deseja sair?", "Sair", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            frame.dispose();
        }
    };

    ActionListener translateImageListener = e -> {
        if (originalImage != null) {
            String xInput = JOptionPane.showInputDialog(frame, "Digite a coordenada X de translação:");
            String yInput = JOptionPane.showInputDialog(frame, "Digite a coordenada Y de translação:");
            try {
                int xTranslation = Integer.parseInt(xInput);
                int yTranslation = Integer.parseInt(yInput);

                transformedImage = Translate.process(originalImage, xTranslation, yTranslation);
                transformedImageLabel.setIcon(new ImageIcon(transformedImage));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Coordenadas inválidas. Por favor, insira números inteiros.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de realizar uma operação de translação.");
        }
    };

    ActionListener rotateImageListener = e -> {
        if (originalImage != null) {
            String angleInput = JOptionPane.showInputDialog(frame, "Digite o ângulo de rotação (em graus):");
            try {
                transformedImage = Rotate.process(originalImage, Integer.parseInt(angleInput));
                transformedImageLabel.setIcon(new ImageIcon(transformedImage));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ângulo inválido. Por favor, insira um número válido.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de realizar uma operação de rotação.");
        }
    };

    ActionListener mirrorImageListener = e -> {
        String[] options = {"Horizontal", "Vertical"};
        int choice = JOptionPane.showOptionDialog(frame, "Qual forma de espelhamento deseja?",
                "Espelhamento", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) {
            System.out.println("Nenhuma opção selecionada.");
        } else {
            switch (choice) {
                case 0 -> horizontalMirror();
                case 1 -> verticalMirror();
                default -> System.out.println("Opção desconhecida selecionada.");
            }
        }
    };

    ActionListener zoomInListener = e -> {
        if (originalImage != null) {
            double zoomFactor = 2;
            transformedImage = Zoom.process(originalImage, zoomFactor, zoomFactor);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de realizar o aumento.");
        }
    };

    ActionListener zoomOutListener = e -> {
        if (originalImage != null) {
            double zoomFactor = 0.5;
            transformedImage = Zoom.process(originalImage, zoomFactor, zoomFactor);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de realizar a diminuição.");
        }
    };

    ActionListener brightnessListener = e -> {
        if (originalImage != null) {
            int brightness = 90;
            transformedImage = Brightness.process(originalImage, brightness);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de aplicar o brilho");
        }
    };

    ActionListener contrastListener = e -> {
        if (originalImage != null) {
            double contrast = 1.5;
            transformedImage = Contrast.process(originalImage, contrast);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de aplicar o contraste");
        }
    };

    ActionListener grayscaleListener = e -> {
        if (originalImage != null) {
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            transformedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = originalImage.getRGB(x, y);

                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;

                    int gray = (red + green + blue) / 3;

                    int newRGB = (gray << 16) | (gray << 8) | gray;

                    transformedImage.setRGB(x, y, newRGB);
                }
            }
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de realizar uma operação de transformação.");
        }
    };

    ActionListener lowPassListener = e -> {
        String[] options = {"Média", "Moda", "Mediana", "Gauss"};
        int choice = JOptionPane.showOptionDialog(frame, "Qual filtro deseja aplicar?",
                "Filtro passa baixa", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) {
            System.out.println("Nenhuma opção selecionada.");
        } else {
            switch (choice) {
                case 0 -> handleAverageFilter();
                case 1 -> handleModeFilter();
                case 2 -> handleMedianFilter();
                case 3 -> handleGaussFilter();
                default -> System.out.println("Opção desconhecida selecionada.");
            }
        }
    };

    ActionListener highPassListener = e -> {
        if (originalImage != null) {
            transformedImage = Kirsch.process(originalImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, Constants.OPEN_IMAGE);
        }
    };

    ActionListener dilationListener = e -> {
        if (originalImage != null) {
            transformedImage = Dilation.process(originalImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, Constants.OPEN_IMAGE);
        }
    };

    ActionListener erosionListener = e -> {
        if (originalImage != null) {
            transformedImage = Erosion.process(originalImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, Constants.OPEN_IMAGE);
        }
    };

    ActionListener openingListener = e -> {
        if (originalImage != null) {
            BufferedImage erosionResultImage = Erosion.process(originalImage);
            transformedImage = Dilation.process(erosionResultImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, Constants.OPEN_IMAGE);
        }
    };

    ActionListener closingListener = e -> {
        if (originalImage != null) {
            BufferedImage dilationResultImage = Dilation.process(originalImage);
            transformedImage = Erosion.process(dilationResultImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, Constants.OPEN_IMAGE);
        }
    };

    ActionListener thresholdActionListener = e -> {
        if (originalImage != null) {
            transformedImage = Threshold.process(originalImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, Constants.OPEN_IMAGE);
        }
    };

    ActionListener challengeListener = e -> new ChallengeFrame(frame);

    private void horizontalMirror() {
        if (originalImage != null) {
            transformedImage = HorizontalMirror.process(originalImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de realizar o espelhamento horizontal.");
        }
    }
    private void verticalMirror() {
        if (originalImage != null) {
            transformedImage = VerticalMirror.process(originalImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de realizar o espelhamento vertical.");
        }
    }

    private void handleAverageFilter() {
        if (originalImage != null) {
            transformedImage = AverageLowFilter.process(originalImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, Constants.OPEN_IMAGE);
        }
    }
    private void handleModeFilter() {
        if (originalImage != null) {
            transformedImage = ModeLowFilter.process(originalImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, Constants.OPEN_IMAGE);
        }
    }
    private void handleMedianFilter() {
        if (originalImage != null) {
            transformedImage = MedianLowFilter.process(originalImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, Constants.OPEN_IMAGE);
        }
    }
    private void handleGaussFilter() {
        if (originalImage != null) {
            transformedImage = GaussLowFilter.process(originalImage);
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, Constants.OPEN_IMAGE);
        }
    }


    public static void main(String[] args) {
        new CorelDraw();
    }
}

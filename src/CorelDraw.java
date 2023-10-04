import procedures.AverageLowFilter;
import procedures.GaussLowFilter;
import procedures.MedianLowFilter;
import procedures.ModeLowFilter;
import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

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
        zoomIn.addActionListener(zoomOutListener);

        geometricTransformMenu.add(translate);
        geometricTransformMenu.add(rotate);
        geometricTransformMenu.add(mirror);
        geometricTransformMenu.add(zoomIn);
        geometricTransformMenu.add(zoomOut);

        return geometricTransformMenu;
    }

    public JMenu createFiltersMenu () {
        JMenu filtersMenu = new JMenu("Filtros");
        JMenuItem grayscale = new JMenuItem("Grayscale");
        JMenuItem lowPass = new JMenuItem("Passa Baixa");
        JMenuItem highPass = new JMenuItem("Passa Alta");
        JMenuItem threshold = new JMenuItem("Threshold");

        grayscale.addActionListener(grayscaleListener);
        lowPass.addActionListener(lowPassListener);

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

        morphologyMenu.add(dilation);
        morphologyMenu.add(erosion);
        morphologyMenu.add(opening);
        morphologyMenu.add(closing);

        return morphologyMenu;
    }

    public JMenu createFeatureExtractionMenu () {
        JMenu featureExtractionMenu = new JMenu("Extração de Características");
        JMenuItem challengeItem = new JMenuItem("DESAFIO");

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
                    // Verifique a extensão do arquivo escolhido
                    String fileName = selectedFile.getName().toLowerCase();
                    if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
                        fileName += ".png"; // Defina o formato de imagem padrão para PNG se a extensão não estiver especificada
                    }

                    // Salve a imagem transformada no arquivo selecionado
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
            frame.dispose(); // Fecha a janela
        }
    };

    ActionListener translateImageListener = e -> {
        if (originalImage != null) {
            String xInput = JOptionPane.showInputDialog(frame, "Digite a coordenada X de translação:");
            String yInput = JOptionPane.showInputDialog(frame, "Digite a coordenada Y de translação:");
            try {
                // Converta as coordenadas de entrada em valores inteiros
                int xTranslation = Integer.parseInt(xInput);
                int yTranslation = Integer.parseInt(yInput);

                int width = originalImage.getWidth();
                int height = originalImage.getHeight();
                transformedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

                // Matriz de translação
                double[][] translationMatrix = {
                        {1, 0, xTranslation},
                        {0, 1, yTranslation},
                        {0, 0, 1}
                };

                // Aplicar a translação na imagem original
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int cor = originalImage.getRGB(x, y);

                        // Aplicar a transformação usando multiplicação de matrizes
                        double[] pixel = {x, y, 1};
                        double[] newPixel = new double[3];

                        for (int i = 0; i < 3; i++) {
                            newPixel[i] = 0;
                            for (int j = 0; j < 3; j++) {
                                newPixel[i] += translationMatrix[i][j] * pixel[j];
                            }
                        }

                        int novoX = (int) newPixel[0];
                        int novoY = (int) newPixel[1];

                        if (novoX >= 0 && novoX < width && novoY >= 0 && novoY < height) {
                            transformedImage.setRGB(novoX, novoY, cor);
                        }
                    }
                }
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
                // Converta o ângulo de entrada em radianos
                double rotationAngle = Math.toRadians(Double.parseDouble(angleInput));

                int width = originalImage.getWidth();
                int height = originalImage.getHeight();
                transformedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

                // Matriz de rotação
                double cosTheta = Math.cos(rotationAngle);
                double sinTheta = Math.sin(rotationAngle);
                double[][] rotationMatrix = {
                        {cosTheta, -sinTheta, 0},
                        {sinTheta, cosTheta, 0},
                        {0, 0, 1}
                };

                // Coordenadas do ponto de rotação (centro da imagem)
                int centerX = width / 2;
                int centerY = height / 2;

                // Aplicar a rotação na imagem original
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int cor = originalImage.getRGB(x, y);

                        // Aplicar a transformação usando multiplicação de matrizes
                        double[] pixel = {x - centerX, y - centerY, 1};
                        double[] newPixel = new double[3];

                        for (int i = 0; i < 3; i++) {
                            newPixel[i] = 0;
                            for (int j = 0; j < 3; j++) {
                                newPixel[i] += rotationMatrix[i][j] * pixel[j];
                            }
                        }

                        int novoX = (int) (newPixel[0] + centerX);
                        int novoY = (int) (newPixel[1] + centerY);

                        if (novoX >= 0 && novoX < width && novoY >= 0 && novoY < height) {
                            transformedImage.setRGB(novoX, novoY, cor);
                        }
                    }
                }
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
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            double zoomFactor = 1.5; // Fator de aumento

            transformedImage = new BufferedImage((int) (width * zoomFactor), (int) (height * zoomFactor), BufferedImage.TYPE_INT_ARGB);

            // Matriz de transformação de aumento
            double[][] zoomMatrix = {
                    {zoomFactor, 0, 0},
                    {0, zoomFactor, 0},
                    {0, 0, 1}
            };

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int cor = originalImage.getRGB(x, y);

                    // Aplicar a transformação usando multiplicação de matrizes
                    double[] pixel = {x, y, 1};
                    double[] newPixel = new double[3];

                    for (int i = 0; i < 3; i++) {
                        newPixel[i] = 0;
                        for (int j = 0; j < 3; j++) {
                            newPixel[i] += zoomMatrix[i][j] * pixel[j];
                        }
                    }

                    int novoX = (int) newPixel[0];
                    int novoY = (int) newPixel[1];

                    if (novoX >= 0 && novoX < transformedImage.getWidth() && novoY >= 0 && novoY < transformedImage.getHeight()) {
                        transformedImage.setRGB(novoX, novoY, cor);
                    }
                }
            }
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de realizar o aumento.");
        }
    };

    ActionListener zoomOutListener = e -> {
        if (originalImage != null) {
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            double zoomFactorX = 0.5; // Fator de diminuição horizontal
            double zoomFactorY = 0.5; // Fator de diminuição vertical

            int newWidth = (int) (width * zoomFactorX);
            int newHeight = (int) (height * zoomFactorY);

            transformedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

            // Matriz de transformação de diminuição
            double[][] zoomMatrix = {
                    {zoomFactorX, 0, 0},
                    {0, zoomFactorY, 0},
                    {0, 0, 1}
            };

            for (int y = 0; y < newHeight; y++) {
                for (int x = 0; x < newWidth; x++) {
                    double[] pixel = {x, y, 1};
                    double[] newPixel = new double[3];

                    for (int i = 0; i < 3; i++) {
                        newPixel[i] = 0;
                        for (int j = 0; j < 3; j++) {
                            newPixel[i] += zoomMatrix[i][j] * pixel[j];
                        }
                    }

                    int origX = (int) newPixel[0];
                    int origY = (int) newPixel[1];

                    if (origX >= 0 && origX < width && origY >= 0 && origY < height) {
                        int color = originalImage.getRGB(origX, origY);
                        transformedImage.setRGB(x, y, color);
                    }
                }
            }
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de realizar a diminuição.");
        }
    };

    ActionListener grayscaleListener = e -> {
        if (originalImage != null) {
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            // Crie uma cópia da imagem original
            transformedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Percorra cada pixel da imagem
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = originalImage.getRGB(x, y);

                    // Extraia os componentes de cor
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;

                    // Calcule a média dos componentes para obter o valor de cinza
                    int gray = (red + green + blue) / 3;

                    // Crie um novo valor RGB com as três componentes iguais (grayscale)
                    int newRGB = (gray << 16) | (gray << 8) | gray;

                    // Defina o novo valor RGB no pixel da imagem transformada
                    transformedImage.setRGB(x, y, newRGB);
                }
            }
            // Atualize a label da imagem transformada
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

    private void horizontalMirror() {
        if (originalImage != null) {
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            transformedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // Matriz de espelhamento horizontal
            double[][] horizontalMatrix = {
                    {-1, 0, width - 1},
                    {0, 1, 0},
                    {0, 0, 1}
            };

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int cor = originalImage.getRGB(x, y);

                    // Aplicar a transformação usando multiplicação de matrizes
                    double[] pixel = {x, y, 1};
                    double[] newPixel = new double[3];

                    for (int i = 0; i < 3; i++) {
                        newPixel[i] = 0;
                        for (int j = 0; j < 3; j++) {
                            newPixel[i] += horizontalMatrix[i][j] * pixel[j];
                        }
                    }

                    int novoX = (int) newPixel[0];
                    int novoY = (int) newPixel[1];

                    if (novoX >= 0 && novoX < width && novoY >= 0 && novoY < height) {
                        transformedImage.setRGB(novoX, novoY, cor);
                    }
                }
            }
            transformedImageLabel.setIcon(new ImageIcon(transformedImage));
        } else {
            JOptionPane.showMessageDialog(frame, "Abra uma imagem antes de realizar o espelhamento horizontal.");
        }
    }
    private void verticalMirror() {
        if (originalImage != null) {
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            transformedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // Matriz de espelhamento vertical
            double[][] verticalMatrix = {
                    {1, 0, 0},
                    {0, -1, height - 1},
                    {0, 0, 1}
            };

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int cor = originalImage.getRGB(x, y);

                    // Aplicar a transformação usando multiplicação de matrizes
                    double[] pixel = {x, y, 1};
                    double[] newPixel = new double[3];

                    for (int i = 0; i < 3; i++) {
                        newPixel[i] = 0;
                        for (int j = 0; j < 3; j++) {
                            newPixel[i] += verticalMatrix[i][j] * pixel[j];
                        }
                    }

                    int novoX = (int) newPixel[0];
                    int novoY = (int) newPixel[1];

                    if (novoX >= 0 && novoX < width && novoY >= 0 && novoY < height) {
                        transformedImage.setRGB(novoX, novoY, cor);
                    }
                }
            }
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

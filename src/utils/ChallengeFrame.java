package utils;

import procedures.Challenge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

public class ChallengeFrame extends JFrame {

    private JPanel[] panels = new JPanel[3];
    private JLabel[] imageLabels = new JLabel[3];
    private JButton[] buttons = new JButton[3];
    private BufferedImage[] images = new BufferedImage[3];

    public ChallengeFrame(JFrame originalFrame) {
        setLayout(new GridLayout(1, 3)); // Cria uma grade com 1 linha e 3 colunas
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fechar apenas este frame ao ser fechado
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE); // Permite sobrepor o frame original

        for (int i = 0; i < 3; i++) {
            panels[i] = new JPanel();
            panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.Y_AXIS));

            imageLabels[i] = new JLabel();
            if (i == 1) {
                buttons[i] = new JButton("Selecionar Operador");
            } else {
                buttons[i] = new JButton("Selecionar Número");
            }
            buttons[i].setSize(200, 100);
            int finalI = i;
            buttons[i].addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage image = ImageIO.read(selectedFile);
                        images[finalI] = image;
                        ImageIcon icon = new ImageIcon(image);
                        if (finalI == 1) {
                            int larguraOriginal = icon.getIconWidth();
                            int alturaOriginal = icon.getIconHeight();

                            // Defina a largura desejada para a nova imagem
                            int novaLargura = 200; // Altere conforme necessário

                            // Calcule a nova altura mantendo a proporção
                            int novaAltura = (alturaOriginal * novaLargura) / larguraOriginal;

                            Image imagemRedimensionada = icon.getImage().getScaledInstance(
                                    novaLargura, novaAltura, Image.SCALE_SMOOTH);

                            // Crie um novo ImageIcon com a imagem redimensionada
                            icon = new ImageIcon(imagemRedimensionada);

                        }
                        imageLabels[finalI].setIcon(icon);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            panels[i].add(imageLabels[i]);
            panels[i].add(buttons[i]);

            add(panels[i], BorderLayout.CENTER);
        }

        // Configurar o frame original como pai
        if (originalFrame != null) {
            setLocationRelativeTo(originalFrame);
        }
        JButton process = new JButton("Processar");
        process.addActionListener(processListener);
        add(process, BorderLayout.SOUTH);
        setVisible(true);
    }

    ActionListener processListener = e -> {
        String[] values = new String[3];
        if (images.length == 3) {
            for (int i = 0 ; i < images.length ; i++){
                List<Integer> histogram = Challenge.process(images[i]);
                String value = "";
                if (i == 1) {
                    value = Challenge.identifyOperator(histogram);
                } else {
                    value = Challenge.identify(histogram);
                }
                values[i] = value;
            }

            JOptionPane.showMessageDialog(this, Challenge.doMath(values));

        } else {
            JOptionPane.showMessageDialog(this, "Adicione imagem em todos os campos");
        }
    };
}

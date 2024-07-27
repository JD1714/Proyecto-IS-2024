package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import Registro.AdminRegistrationForm;

public class LoginApp {
    public LoginApp() {
        // Crear y configurar el marco
        JFrame frame = new JFrame("Login");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Crear y configurar el encabezado
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.BLACK);
        JLabel headerLabel = new JLabel("Sistema GCX - Login");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Crear un panel para el contenido
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(204, 153, 255)); // Morado claro

        // Configurar restricciones de GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Espaciado alrededor de los componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Llenar solo horizontalmente
        gbc.weightx = 1.0; // Expandirse en el eje horizontal
        gbc.weighty = 0.1; // Expandirse verticalmente

        // Crear y añadir componentes
        JLabel emailLabel = new JLabel("Correo:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 25)); // Tamaño del campo de texto
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas
        contentPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 25)); // Tamaño del campo de contraseña
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Ocupa dos columnas
        contentPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Iniciar sesión");
        // Configuración del tamaño preferido del botón
        loginButton.setPreferredSize(new Dimension(150, 40)); // Tamaño del botón
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.weighty = 0; // Asegura que el botón se mantenga en la parte inferior
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(loginButton, gbc);

        // Añadir acción al botón de inicio de sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                handleLogin(email, password);
            }
        });

        // Crear un panel para los enlaces
        JPanel linksPanel = new JPanel();
        linksPanel.setBackground(new Color(204, 153, 255)); // Morado claro
        linksPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Enlace "No tienes cuenta? Regístrate aquí"
        JLabel registerLink = new JLabel("<html><a href=''>No tienes cuenta? Regístrate aquí</a></html>");
        registerLink.setForeground(Color.BLUE);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Acción al hacer clic en el enlace
                new AdminRegistrationForm(); // Crear y mostrar la interfaz de registro de administrador
                frame.dispose(); // Cerrar el formulario actual
            }
        });
        linksPanel.add(registerLink);

        // Añadir los paneles al marco
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.add(linksPanel, BorderLayout.SOUTH);

        // Mostrar el marco
        frame.setVisible(true);
    }

    // Lógica para manejar el inicio de sesión
    private void handleLogin(String email, String password) {
        if (verificarCredenciales(email, password)) {
            JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
            // Aquí puedes abrir la siguiente ventana o hacer otras acciones necesarias
        } else {
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas.");
        }
    }

    private boolean verificarCredenciales(String correo, String contraseña) {
        return verificarEnArchivo(correo, contraseña, "proponentes.txt") ||
               verificarEnArchivo(correo, contraseña, "administradores.txt");
    }

    private boolean verificarEnArchivo(String correo, String contraseña, String archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("Correo: ")) {
                    if (linea.substring(8).trim().equals(correo)) {
                        linea = br.readLine(); // Leer la línea de contraseña
                        if (linea != null && linea.startsWith("Contraseña: ")) {
                            if (linea.substring(12).trim().equals(contraseña)) {
                                // Saltar cualquier línea adicional hasta el final del bloque
                                while ((linea = br.readLine()) != null && !linea.trim().isEmpty()) {
                                    // Para el archivo de administradores, los datos adicionales no afectan la verificación
                                }
                                return true;
                            }
                        }
                    }
                }
                // Saltar líneas hasta el próximo bloque
                while ((linea = br.readLine()) != null && !linea.startsWith("--------------")) {
                    // Aquí podrías imprimir si necesitas depurar
                    // System.out.println(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginApp();
            }
        });
    }
}

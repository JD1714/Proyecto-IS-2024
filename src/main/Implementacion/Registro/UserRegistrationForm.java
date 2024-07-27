package Registro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.regex.Pattern;
import Login.LoginApp;

public class UserRegistrationForm {
    public UserRegistrationForm() {
        // Crear y configurar el marco
        JFrame frame = new JFrame("Registro de Usuario");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Crear y configurar el encabezado
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.BLACK);
        JLabel headerLabel = new JLabel("Sistema GCX - Registro de Usuario");
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
        gbc.fill = GridBagConstraints.BOTH; // Llenar tanto horizontal como vertical
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
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas
        contentPanel.add(emailField, gbc);

        JLabel confirmEmailLabel = new JLabel("Confirmar Correo:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(confirmEmailLabel, gbc);

        JTextField confirmEmailField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Ocupa dos columnas
        contentPanel.add(confirmEmailField, gbc);

        JLabel passwordLabel = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Ocupa dos columnas
        contentPanel.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirmar Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        contentPanel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Ocupa dos columnas
        contentPanel.add(confirmPasswordField, gbc);

        JButton registerButton = new JButton("Registrar");
        // Configuración del tamaño preferido del botón
        registerButton.setPreferredSize(new Dimension(120, 40)); // Tamaño del botón
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.weighty = 0; // Asegura que el botón se mantenga en la parte inferior
        contentPanel.add(registerButton, gbc);

        // Añadir acción al botón de registro
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String confirmEmail = confirmEmailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                handleRegister(email, confirmEmail, password, confirmPassword);
            }
        });

        // Crear un panel para los enlaces
        JPanel linksPanel = new JPanel();
        linksPanel.setBackground(new Color(204, 153, 255)); // Morado claro
        linksPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Enlace "Registro Administrador"
        JLabel adminRegisterLink = new JLabel("<html><a href=''>Registro Administrador</a></html>");
        adminRegisterLink.setForeground(Color.BLUE);
        adminRegisterLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminRegisterLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Acción al hacer clic en el enlace
                new AdminRegistrationForm(); // Crear y mostrar la interfaz de registro de administrador
                frame.dispose(); // Cerrar el formulario actual
            }
        });
        linksPanel.add(adminRegisterLink);

        // Enlace "Ya tienes cuenta? Ingresa aquí"
        JLabel loginLink = new JLabel("<html><a href=''>Ya tienes cuenta? Ingresa aquí</a></html>");
        loginLink.setForeground(Color.BLUE);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginApp();
            }
        });
        linksPanel.add(loginLink);

        // Añadir los paneles al marco
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.add(linksPanel, BorderLayout.SOUTH);

        // Mostrar el marco
        frame.setVisible(true);
    }

    // Lógica para manejar el registro
    private static void handleRegister(String email, String confirmEmail, String password, String confirmPassword) {
        // Validar que el correo y la contraseña coincidan
        if (!email.equals(confirmEmail)) {
            JOptionPane.showMessageDialog(null, "Los correos no coinciden. Por favor, inténtelo de nuevo.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden. Por favor, inténtelo de nuevo.");
            return;
        }

        // Validar que los campos no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }

        // Validar formato de correo electrónico
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "El correo electrónico no tiene un formato válido.");
            return;
        }

        // Verificar si el correo electrónico ya está en el archivo
        if (isEmailAlreadyRegistered(email)) {
            JOptionPane.showMessageDialog(null, "El correo electrónico ya está registrado.");
            return;
        }

        // Guardar los datos en un archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("proponentes.txt", true))) {
            writer.write("Correo: " + email);
            writer.newLine();
            writer.write("Contraseña: " + password);
            writer.newLine();
            writer.write("--------------");
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos. Por favor, inténtelo de nuevo.");
            ex.printStackTrace();
        }
    }

    // Verificar si el correo electrónico ya está registrado
    private static boolean isEmailAlreadyRegistered(String email) {
        File file = new File("proponentes.txt");
        if (!file.exists()) {
            return false; // El archivo no existe, por lo tanto el correo no está registrado
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Correo: " + email)) {
                    return true; // El correo ya está registrado
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false; // El correo no está registrado
    }

    // Validar formato de correo electrónico
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }
}
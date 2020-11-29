package cliente;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Ventana implements ActionListener{
    public static String Nombre;
    public  int statusOp;
    public int salida;
    private JFrame ventana;
    private JTabbedPane chats;
    private JPanel panel;
    private ArrayList<JTextArea> chatPersona;
    private ArrayList<String> contactos;
    private ArrayList<String> contactosChat;
    private ArrayList<JTextField> textoEnviar;
    private ArrayList<JButton> botonesEnviar;
    
    public Ventana(int operacion){
        statusOp = operacion;
        salida = 0;
        Nombre = JOptionPane.showInputDialog("Ingresa el nombre con el que te deseas identificar");
        while(Nombre.isEmpty()){
            Nombre = JOptionPane.showInputDialog("Ingresa el nombre con el que te deseas identificar");
        }
        ventana = new JFrame();
        
        ventana.setSize(500, 500);
        ventana.setTitle(Nombre + ": Chat multicast");
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(3);
        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                statusOp = 1;
                salida = 1;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        chatPersona = new ArrayList();
        contactosChat = new ArrayList();
        contactos = new ArrayList();
        textoEnviar = new ArrayList();
        botonesEnviar = new ArrayList();
        
        inicializarComponentes();
        ventana.setVisible(true);
    }
    
    private void inicializarComponentes(){
        panel = new JPanel();
        JLabel titulo = new JLabel("Tus chats: " + Nombre);
        
        panel.setLayout(null);
        ventana.getContentPane().add(panel);
        titulo.setBounds(10, 10, 180, 30);
        panel.add(titulo);
        
        setButtons();
        setGeneral();
    }
    
    private void setButtons(){
        JButton newChat = new JButton();
        newChat.addActionListener(this);
        newChat.setText("Nuevo chat privado");
        newChat.setBounds(290, 10, 200, 30);
        panel.add(newChat);
    }
    
    private void setGeneral(){
        chats = new JTabbedPane();
        chats.setBounds(10, 50, 480, 400);
        newChat("General");
        //newChat("Diana");
        panel.add(chats);
    }
    
    private void newChat(String nombre){
        JPanel newPanel = new JPanel();
        newPanel.setLayout(null);
        chats.addTab(nombre, newPanel);
        
        JTextArea conversacion = new JTextArea();
        conversacion.setEditable(false);
        conversacion.setForeground(Color.BLACK);
        chatPersona.add(conversacion);
        JScrollPane scroll = new JScrollPane(chatPersona.get(chatPersona.size() - 1));
        scroll.setBounds(10,10,455,300);
        newPanel.add(scroll);
        
        JTextField texto = new JTextField();
        textoEnviar.add(texto);
        textoEnviar.get(textoEnviar.size() - 1).setBounds(10, 320, 350, 39);
        newPanel.add(textoEnviar.get(textoEnviar.size() - 1));
        
        JButton enviar = new JButton("Enviar");
        botonesEnviar.add(enviar);
        botonesEnviar.get(botonesEnviar.size() - 1).setBounds(370, 320, 95, 38);
        botonesEnviar.get(botonesEnviar.size() - 1).setText("Enviar");
        botonesEnviar.get(botonesEnviar.size() - 1).addActionListener(this);
        String butname = "Enviar" + (botonesEnviar.size() - 1);
        System.out.println(butname);
        botonesEnviar.get(botonesEnviar.size() - 1).setName(butname);
        newPanel.add(botonesEnviar.get(botonesEnviar.size() - 1));
        
        contactosChat.add(nombre);
    }
    
    public int getStatus(){
        return statusOp;
    }
    
    public int getSalida(){
        return salida;
    }
    
    public void setStatus(int newStatus){
        statusOp = newStatus;
    }
    
    public String getActiveMessage(){
        int selectedIndex = chats.getSelectedIndex();
        String texto = textoEnviar.get(selectedIndex).getText();
        textoEnviar.get(selectedIndex).setText("");
        return texto;
    }
    
    public int getActiveTab(){
        
        return chats.getSelectedIndex();
    }
    
    public String getContactosChat(int indice){
        return contactosChat.get(indice);
    }
    
    public void setNewMessage(String mensaje){
        if(mensaje.contains("<contactos>")){
            String contacto = "";
            contactos.clear();
            for(int i = 12; i < mensaje.length(); i++){
                System.out.println("|" + mensaje.charAt(i) + "|");
                //Thread.sleep(1000);
                if(Character.isLetter(mensaje.charAt(i))){                    
                    contacto += mensaje.charAt(i);
                }else if(mensaje.charAt(i) == ','){
                    contactos.add(contacto);
                    contacto = "";
                }else{
                    //break;
                }
            }
            contactos.add(contacto);
            System.out.println(contactos);
            //contactos.remove(Nombre);
        }else if(mensaje.startsWith("S<msj>")){
            String remitente = "";
            mensaje = mensaje.substring(6);
            
            //char feliz = (char) "\u1F600";
            
            mensaje = mensaje.replace(":)", "\uD83D\uDE04");
            mensaje = mensaje.replace(":D", "\uD83D\uDE03");
            mensaje = mensaje.replace(":3", "\uD83D\uDE0A");
            mensaje = mensaje.replace(":P", "\uD83D\uDE1C");
            mensaje = mensaje.replace(":(", "\uD83D\uDE14");
            mensaje = mensaje.replace(":'(", "\uD83D\uDE22");
            mensaje = mensaje.replace("D:", "\uD83D\uDE29");
            mensaje = mensaje.replace(">:c", "\uD83D\uDE21");
            
            
            if(mensaje.contains("<privado>")){
                String destinatario = "";
                int i = 1;
                
                mensaje = mensaje.substring(9);
                while(Character.isLetter(mensaje.charAt(i))){
                    remitente = remitente + mensaje.charAt(i);
                    i++;
                }
                mensaje = mensaje.substring(i + 1);
                
                i = 1;
                System.out.println(mensaje);
                while(Character.isLetter(mensaje.charAt(i))){
                    destinatario = destinatario + mensaje.charAt(i);
                    i++;
                }
                mensaje = mensaje.substring(i + 1);
                System.out.println(mensaje);
                System.out.println("Remitente: " + remitente + " destinatario: " + destinatario);
                
                if(Nombre.equals(destinatario)){
                    if(contactosChat.contains(remitente)){
                        int selectedIndex = contactosChat.indexOf(remitente);
                        textoEnviar.get(selectedIndex).setText("");
                        chatPersona.get(selectedIndex).setText(chatPersona.get(selectedIndex).getText() + "\n" + remitente + ":" + mensaje);
                    }else{
                        newChat(remitente);
                        chats.setSelectedIndex(contactosChat.indexOf(remitente));
                        int selectedIndex = chats.getSelectedIndex();
                        textoEnviar.get(selectedIndex).setText("");
                        chatPersona.get(selectedIndex).setText(chatPersona.get(selectedIndex).getText() + "\n" + remitente + ":" + mensaje);
                    }
                }else if(Nombre.equals(remitente)){
                    int selectedIndex = chats.getSelectedIndex();
                    textoEnviar.get(selectedIndex).setText("");
                    chatPersona.get(selectedIndex).setText(chatPersona.get(selectedIndex).getText() + "\n" + remitente + ":" + mensaje);
                }
            }else{
                int selectedIndex = 0;
                textoEnviar.get(selectedIndex).setText("");
                chatPersona.get(selectedIndex).setText(chatPersona.get(selectedIndex).getText() + "\n" + mensaje);
            }
        }
    }
    
    public String getNombre(){
        return Nombre;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        JButton boton = (JButton) ae.getSource();
        
        if(boton.getActionCommand().equals("Nuevo chat privado")){
            if(contactos.toArray().length == 0){
                JOptionPane.showMessageDialog(null, "En este momento no hay contactos en linea", "Contactos", JOptionPane.ERROR_MESSAGE);
            }else{
                ArrayList<String> contactosMostrar = contactos;
                contactosMostrar.remove(Nombre);
                String seleccion = (String) JOptionPane.showInputDialog(null, "Nuevo chat", "Seleccione el usuario con el que desea crear un chat", JOptionPane.INFORMATION_MESSAGE, null, contactosMostrar.toArray(), contactosMostrar.toArray()[0]);
                if(contactosChat.contains(seleccion)){
                    chats.setSelectedIndex(contactosChat.indexOf(seleccion));
                }else{
                    newChat(seleccion);
                    chats.setSelectedIndex(contactosChat.indexOf(seleccion));
                }
                statusOp = 0;
            }
        }else if(boton.getActionCommand().equals("Enviar")){
            statusOp = 1;
        }
    }
}

package com.vuelosfis.model;

public class EmailNotificationService implements INotificationService {
    @Override
    public void notificar(String mensaje, String destinatario) {
        System.out.println("[EMAIL SENT] To: " + destinatario + " | Content: " + mensaje);
    }
}

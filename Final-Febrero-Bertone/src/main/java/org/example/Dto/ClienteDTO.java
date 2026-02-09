package org.example.Dto;
import org.example.models.Cliente;

public class ClienteDTO {
    private int idCliente;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String vehiculo;
    private boolean activo;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    //metodo para convertir de DTO a entidad
    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(this.idCliente);
        cliente.setNombre(this.nombre);
        cliente.setApellido(this.apellido);
        cliente.setTelefono(this.telefono);
        cliente.setEmail(this.email);
        cliente.setVehiculo(this.vehiculo);
        cliente.setActivo(this.activo);
        return cliente;
    }
    //metodo para convertir de entidad a DTO
    public static ClienteDTO fromEntity(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        ClienteDTO DTO = new ClienteDTO();
        DTO.setIdCliente(cliente.getIdCliente());
        DTO.setNombre(cliente.getNombre());
        DTO.setApellido(cliente.getApellido());
        DTO.setTelefono(cliente.getTelefono());
        DTO.setEmail(cliente.getEmail());
        DTO.setVehiculo(cliente.getVehiculo());
        DTO.setActivo(cliente.isActivo());
        return DTO;
    }
}

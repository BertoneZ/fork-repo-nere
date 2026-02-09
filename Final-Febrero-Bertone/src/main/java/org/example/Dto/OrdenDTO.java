package org.example.Dto;
import org.example.models.EstadoOrden;
import org.example.models.Orden;


import java.time.LocalDateTime;
import java.util.Date;
public class OrdenDTO {
    private String descripcionProblema;
    private String fecha;
    private ClienteDTO cliente;
    private LocalDateTime fechaIngreso;
    private String estado;
    private int idOrden;

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    //metodo para convertir de DTO a entidad
    public Orden toEntity() {
        Orden orden = new Orden();
        orden.setDescripcion(this.descripcionProblema);
        orden.setFecha(this.fechaIngreso);
        orden.setCliente(this.cliente.toEntity());
        orden.setEstado(EstadoOrden.valueOf(this.estado));
        return orden;
    }
    //metodo para convertir de entidad a DTO
    public static OrdenDTO fromEntity(Orden orden) {
        if (orden == null) {
            return null;
        }
        OrdenDTO DTO = new OrdenDTO();
        DTO.setIdOrden(orden.getIdOrden());
        DTO.setDescripcionProblema(orden.getDescripcion());
        DTO.setFechaIngreso(orden.getFecha());
        DTO.setCliente(ClienteDTO.fromEntity(orden.getCliente()));
        DTO.setEstado(orden.getEstado().name());
        return DTO;
    }
}

package org.example.models;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;



@Converter(autoApply = true)
public  class EstadoOrdenConverter implements AttributeConverter<EstadoOrden, String> {
    public EstadoOrdenConverter() {
    }

    @Override
    public String convertToDatabaseColumn(EstadoOrden estado) {
        if (estado == null) return null;
        if (estado == EstadoOrden.EnProceso) return "En Proceso"; // Aquí agregamos el espacio para la BD
        return estado.name();
    }

    @Override
    public EstadoOrden convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        // Si en la BD lee "En Proceso", lo transforma al Enum EnProceso de Java
        if (dbData.equals("En Proceso")) return EstadoOrden.EnProceso;
        return EstadoOrden.valueOf(dbData);
    }
}
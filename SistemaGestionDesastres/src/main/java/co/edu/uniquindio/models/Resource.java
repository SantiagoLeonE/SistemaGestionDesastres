package co.edu.uniquindio.models;

/**
 * Modelo para representar recursos de emergencia
 * Incluye diferentes tipos de recursos necesarios en situaciones de desastre
 */
public class Resource {
    private String id;
    private String name;
    private ResourceType type;
    private int quantity;
    private String unit;
    private int minimumStock;
    private String supplier;
    private boolean isPerishable;
    private String expirationDate;

    /**
     * Enum para los tipos de recursos disponibles
     */
    public enum ResourceType {
        FOOD("Alimentos"),
        MEDICINE("Medicinas"),
        WATER("Agua"),
        SHELTER_SUPPLIES("Suministros de Refugio"),
        RESCUE_EQUIPMENT("Equipo de Rescate"),
        MEDICAL_EQUIPMENT("Equipo Médico"),
        CLOTHING("Ropa y Abrigo"),
        FUEL("Combustible"),
        COMMUNICATION("Comunicación"),
        TOOLS("Herramientas");

        private final String displayName;

        ResourceType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    /**
     * Constructor principal
     *
     * @param id Identificador único del recurso
     * @param name Nombre del recurso
     * @param type Tipo de recurso
     * @param quantity Cantidad disponible
     * @param unit Unidad de medida (kg, litros, unidades, etc.)
     */
    public Resource(String id, String name, ResourceType type, int quantity, String unit) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (unit == null || unit.isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty");
        }

        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = Math.max(0, quantity);
        this.unit = unit;
        this.minimumStock = 0;
        this.supplier = "";
        this.isPerishable = false;
        this.expirationDate = "";
    }

    /**
     * Constructor completo con todos los parámetros
     */
    public Resource(String id, String name, ResourceType type, int quantity, String unit,
                    int minimumStock, String supplier, boolean isPerishable, String expirationDate) {
        this(id, name, type, quantity, unit);
        this.minimumStock = Math.max(0, minimumStock);
        this.supplier = supplier != null ? supplier : "";
        this.isPerishable = isPerishable;
        this.expirationDate = expirationDate != null ? expirationDate : "";
    }

    // ==================== GETTERS ====================

    /**
     * Obtener el ID del recurso
     */
    public String getId() {
        return id;
    }

    /**
     * Obtener el nombre del recurso
     */
    public String getName() {
        return name;
    }

    /**
     * Obtener el tipo de recurso
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Obtener la cantidad disponible
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Obtener la unidad de medida
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Obtener el stock mínimo recomendado
     */
    public int getMinimumStock() {
        return minimumStock;
    }

    /**
     * Obtener el proveedor
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * Verificar si el recurso es perecedero
     */
    public boolean isPerishable() {
        return isPerishable;
    }

    /**
     * Obtener la fecha de expiración
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    // ==================== SETTERS ====================

    /**
     * Establecer el nombre del recurso
     */
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    /**
     * Establecer la cantidad
     * No permite valores negativos
     */
    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    /**
     * Establecer la unidad de medida
     */
    public void setUnit(String unit) {
        if (unit != null && !unit.isEmpty()) {
            this.unit = unit;
        }
    }

    /**
     * Establecer el stock mínimo
     */
    public void setMinimumStock(int minimumStock) {
        this.minimumStock = Math.max(0, minimumStock);
    }

    /**
     * Establecer el proveedor
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier != null ? supplier : "";
    }

    /**
     * Establecer si es perecedero
     */
    public void setPerishable(boolean isPerishable) {
        this.isPerishable = isPerishable;
    }

    /**
     * Establecer fecha de expiración
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate != null ? expirationDate : "";
    }

    /**
     * Establecer el tipo de recurso
     */
    public void setType(ResourceType type) {
        if (type != null) {
            this.type = type;
        }
    }

    // ==================== MÉTODOS DE GESTIÓN DE CANTIDAD ====================

    /**
     * Agregar cantidad al recurso
     *
     * @param amount Cantidad a agregar
     * @return true si se agregó exitosamente
     */
    public boolean addQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
            return true;
        }
        return false;
    }

    /**
     * Reducir cantidad del recurso
     *
     * @param amount Cantidad a reducir
     * @return true si había suficiente cantidad, false si no
     */
    public boolean reduceQuantity(int amount) {
        if (amount > 0 && amount <= quantity) {
            quantity -= amount;
            return true;
        }
        return false;
    }

    /**
     * Transferir cantidad a otro recurso del mismo tipo
     *
     * @param other Recurso destino
     * @param amount Cantidad a transferir
     * @return true si la transferencia fue exitosa
     */
    public boolean transferTo(Resource other, int amount) {
        if (other == null || !this.type.equals(other.type)) {
            return false;
        }

        if (this.reduceQuantity(amount)) {
            other.addQuantity(amount);
            return true;
        }
        return false;
    }

    /**
     * Verificar si hay suficiente cantidad
     *
     * @param required Cantidad requerida
     * @return true si hay suficiente
     */
    public boolean hasEnough(int required) {
        return quantity >= required;
    }

    /**
     * Obtener el porcentaje de stock actual respecto al mínimo
     *
     * @return Porcentaje (0-100+)
     */
    public double getStockPercentage() {
        if (minimumStock == 0) {
            return 100.0;
        }
        return (quantity * 100.0) / minimumStock;
    }

    // ==================== MÉTODOS DE VERIFICACIÓN ====================

    /**
     * Verificar si el stock está bajo (menor que el mínimo)
     */
    public boolean isLowStock() {
        return quantity < minimumStock;
    }

    /**
     * Verificar si el stock está crítico (menor al 50% del mínimo)
     */
    public boolean isCriticalStock() {
        return quantity < (minimumStock * 0.5);
    }

    /**
     * Verificar si el recurso está agotado
     */
    public boolean isDepleted() {
        return quantity == 0;
    }

    /**
     * Verificar si el recurso está disponible
     */
    public boolean isAvailable() {
        return quantity > 0;
    }

    /**
     * Obtener cantidad necesaria para alcanzar el stock mínimo
     */
    public int getQuantityNeeded() {
        return Math.max(0, minimumStock - quantity);
    }

    /**
     * Obtener el estado del stock como texto
     */
    public String getStockStatus() {
        if (isDepleted()) {
            return "AGOTADO";
        } else if (isCriticalStock()) {
            return "CRÍTICO";
        } else if (isLowStock()) {
            return "BAJO";
        } else {
            return "NORMAL";
        }
    }

    /**
     * Obtener prioridad de reabastecimiento (1-5)
     * 5 = Máxima prioridad (agotado o crítico)
     * 1 = Baja prioridad (stock normal)
     */
    public int getRestockPriority() {
        if (isDepleted()) {
            return 5;
        } else if (isCriticalStock()) {
            return 4;
        } else if (isLowStock()) {
            return 3;
        } else if (getStockPercentage() < 150) {
            return 2;
        } else {
            return 1;
        }
    }

    // ==================== MÉTODOS DE INFORMACIÓN ====================

    /**
     * Obtener información completa del recurso
     */
    public String getFullInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(name).append(" ===\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Tipo: ").append(type).append("\n");
        sb.append("Cantidad: ").append(quantity).append(" ").append(unit).append("\n");
        sb.append("Stock mínimo: ").append(minimumStock).append(" ").append(unit).append("\n");
        sb.append("Estado: ").append(getStockStatus()).append("\n");
        sb.append("Prioridad reabastecimiento: ").append(getRestockPriority()).append("/5\n");

        if (!supplier.isEmpty()) {
            sb.append("Proveedor: ").append(supplier).append("\n");
        }

        if (isPerishable) {
            sb.append("Perecedero: Sí\n");
            if (!expirationDate.isEmpty()) {
                sb.append("Fecha de expiración: ").append(expirationDate).append("\n");
            }
        }

        return sb.toString();
    }

    // ==================== MÉTODOS SOBRESCRITOS ====================

    /**
     * Representación en String del recurso
     */
    @Override
    public String toString() {
        return name + " - " + quantity + " " + unit + " [" + getStockStatus() + "]";
    }

    /**
     * Comparar recursos por ID
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Resource resource = (Resource) obj;
        return id.equals(resource.id);
    }

    /**
     * Hash code basado en el ID
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Crear una copia del recurso
     */
    public Resource copy() {
        return new Resource(id, name, type, quantity, unit, minimumStock,
                supplier, isPerishable, expirationDate);
    }

    /**
     * Combinar dos recursos del mismo tipo
     * Suma las cantidades si son del mismo tipo
     */
    public static Resource merge(Resource r1, Resource r2) {
        if (r1 == null) return r2;
        if (r2 == null) return r1;

        if (!r1.type.equals(r2.type) || !r1.unit.equals(r2.unit)) {
            throw new IllegalArgumentException("Cannot merge resources of different types or units");
        }

        Resource merged = r1.copy();
        merged.quantity += r2.quantity;
        return merged;
    }
}

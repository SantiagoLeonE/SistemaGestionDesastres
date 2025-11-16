package co.edu.uniquindio.structures;

/**
 * Implementación propia de un Mapa (Hash Table)
 * Similar a HashMap, pero implementada desde cero
 * Utiliza encadenamiento separado para manejar colisiones
 */
public class CustomMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] table;
    private int size;
    private int threshold;

    /**
     * Clase interna para representar una entrada del mapa
     */
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * Constructor: Inicializa el mapa con capacidad inicial
     */
    @SuppressWarnings("unchecked")
    public CustomMap() {
        this.table = new Entry[INITIAL_CAPACITY];
        this.size = 0;
        this.threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    /**
     * Constructor con capacidad inicial personalizada
     */
    @SuppressWarnings("unchecked")
    public CustomMap(int initialCapacity) {
        this.table = new Entry[initialCapacity];
        this.size = 0;
        this.threshold = (int) (initialCapacity * LOAD_FACTOR);
    }

    /**
     * Función hash para distribuir las claves
     * Complejidad: O(1)
     */
    private int hash(K key) {
        if (key == null) return 0;
        int h = key.hashCode();
        // Mejor distribución de bits
        h ^= (h >>> 20) ^ (h >>> 12);
        h ^= (h >>> 7) ^ (h >>> 4);
        return Math.abs(h) % table.length;
    }

    /**
     * Agregar o actualizar un par clave-valor
     * Complejidad: O(1) promedio, O(n) peor caso
     */
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int index = hash(key);
        Entry<K, V> entry = table[index];

        // Buscar si la clave ya existe
        while (entry != null) {
            if (entry.key == null ? key == null : entry.key.equals(key)) {
                entry.value = value; // Actualizar valor existente
                return;
            }
            entry = entry.next;
        }

        // Insertar nueva entrada al inicio de la lista
        table[index] = new Entry<>(key, value, table[index]);
        size++;
    }

    /**
     * Obtener el valor asociado a una clave
     * Complejidad: O(1) promedio, O(n) peor caso
     */
    public V get(K key) {
        int index = hash(key);
        Entry<K, V> entry = table[index];

        while (entry != null) {
            if (entry.key == null ? key == null : entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }

        return null;
    }

    /**
     * Verificar si contiene una clave
     * Complejidad: O(1) promedio
     */
    public boolean containsKey(K key) {
        return get(key) != null || (get(key) == null && containsKeyWithNullValue(key));
    }

    /**
     * Verificar si existe una clave con valor null
     */
    private boolean containsKeyWithNullValue(K key) {
        int index = hash(key);
        Entry<K, V> entry = table[index];

        while (entry != null) {
            if (entry.key == null ? key == null : entry.key.equals(key)) {
                return true;
            }
            entry = entry.next;
        }
        return false;
    }

    /**
     * Remover una entrada
     * Complejidad: O(1) promedio, O(n) peor caso
     */
    public V remove(K key) {
        int index = hash(key);
        Entry<K, V> entry = table[index];
        Entry<K, V> prev = null;

        while (entry != null) {
            if (entry.key == null ? key == null : entry.key.equals(key)) {
                if (prev == null) {
                    table[index] = entry.next;
                } else {
                    prev.next = entry.next;
                }
                size--;
                return entry.value;
            }
            prev = entry;
            entry = entry.next;
        }

        return null;
    }

    /**
     * Obtener todas las claves
     * Complejidad: O(n)
     */
    public CustomList<K> keys() {
        CustomList<K> keyList = new CustomList<>();
        for (Entry<K, V> entry : table) {
            Entry<K, V> current = entry;
            while (current != null) {
                keyList.add(current.key);
                current = current.next;
            }
        }
        return keyList;
    }

    /**
     * Obtener todos los valores
     * Complejidad: O(n)
     */
    public CustomList<V> values() {
        CustomList<V> valueList = new CustomList<>();
        for (Entry<K, V> entry : table) {
            Entry<K, V> current = entry;
            while (current != null) {
                valueList.add(current.value);
                current = current.next;
            }
        }
        return valueList;
    }

    /**
     * Obtener todos los pares clave-valor
     * Complejidad: O(n)
     */
    public CustomList<MapEntry<K, V>> entrySet() {
        CustomList<MapEntry<K, V>> entries = new CustomList<>();
        for (Entry<K, V> entry : table) {
            Entry<K, V> current = entry;
            while (current != null) {
                entries.add(new MapEntry<>(current.key, current.value));
                current = current.next;
            }
        }
        return entries;
    }

    /**
     * Clase para representar una entrada exportada del mapa
     */
    public static class MapEntry<K, V> {
        private K key;
        private V value;

        public MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }
    }

    /**
     * Obtener el tamaño del mapa
     * Complejidad: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Verificar si el mapa está vació
     * Complejidad: O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Redimensionar la tabla cuando se alcanza el factor de carga
     * Complejidad: O(n)
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = new Entry[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);
        size = 0;

        // Reinsertar todas las entradas
        for (Entry<K, V> entry : oldTable) {
            Entry<K, V> current = entry;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    /**
     * Limpiar el mapa
     * Complejidad: O(n)
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        table = new Entry[INITIAL_CAPACITY];
        size = 0;
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    /**
     * Representación en String del mapa
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (Entry<K, V> entry : table) {
            Entry<K, V> current = entry;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.key).append("=").append(current.value);
                first = false;
                current = current.next;
            }
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * Obtener la capacidad actual de la tabla
     */
    public int capacity() {
        return table.length;
    }

    /**
     * Obtener el factor de carga actual
     */
    public double loadFactor() {
        return (double) size / table.length;
    }
}

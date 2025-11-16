package co.edu.uniquindio.structures;

/**
 * Implementación propia de una Lista Dinámica
 * Similar a ArrayList, pero implementada desde cero
 * Utiliza un arreglo interno que se redimensiona automáticamente
 */
public class CustomList<T> {
    private static final int INITIAL_CAPACITY = 10;
    private Object[] elements;
    private int size;

    /**
     * Constructor: Inicializa la lista con capacidad inicial
     */
    public CustomList() {
        this.elements = new Object[INITIAL_CAPACITY];
        this.size = 0;
    }

    /**
     * Agregar un elemento al final de la lista
     * Complejidad: O(1) amortizado
     */
    public void add(T element) {
        ensureCapacity();
        elements[size++] = element;
    }

    /**
     * Agregar un elemento en una posición específica
     * Complejidad: O(n)
     */
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        ensureCapacity();

        // Desplazar elementos hacia la derecha
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;
    }

    /**
     * Obtener un elemento en una posición específica
     * Complejidad: O(1)
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }

    /**
     * Establecer un elemento en una posición específica
     * Complejidad: O(1)
     */
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        elements[index] = element;
    }

    /**
     * Remover un elemento en una posición específica
     * Complejidad: O(n)
     */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        T removedElement = (T) elements[index];

        // Desplazar elementos hacia la izquierda
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[--size] = null; // Ayuda al garbage collector
        return removedElement;
    }

    /**
     * Remover un elemento específico (primera ocurrencia)
     * Complejidad: O(n)
     */
    public boolean remove(T element) {
        int index = indexOf(element);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    /**
     * Verificar si la lista contiene un elemento
     * Complejidad: O(n)
     */
    public boolean contains(T element) {
        return indexOf(element) >= 0;
    }

    /**
     * Obtener el índice de un elemento
     * Complejidad: O(n)
     */
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (element == null ? elements[i] == null : element.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Obtener el tamaño de la lista
     * Complejidad: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Verificar si la lista está vacía
     * Complejidad: O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Limpiar la lista (remover todos los elementos)
     * Complejidad: O(n)
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * Asegurar que hay capacidad suficiente
     * Si el arreglo está lleno, se duplica su capacidad
     * Complejidad: O(n) cuando se redimensiona
     */
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    /**
     * Convertir a arreglo
     * Complejidad: O(n)
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return (T[]) result;
    }

    /**
     * Obtener el último elemento
     * Complejidad: O(1)
     */
    @SuppressWarnings("unchecked")
    public T getLast() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("List is empty");
        }
        return (T) elements[size - 1];
    }

    /**
     * Obtener el primer elemento
     * Complejidad: O(1)
     */
    @SuppressWarnings("unchecked")
    public T getFirst() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("List is empty");
        }
        return (T) elements[0];
    }

    /**
     * Representación en String de la lista
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
package main.java.datatype;

import java.util.*;
import java.lang.reflect.Array;

/**
 * Projeto AED do grupo nº 099
 * 
 * @author 51063 - Miguel Dias
 * @author 51021 - Pedro Marques
 * @author 51074 - Rui Amador
 * 
 * Mutable
 * null values are NOT allowed.
 * @param T the type of the elements of the ArrayNTree
 */
public class ArrayNTree<T extends Comparable<T>> implements NTree<T> {

    private T data;
    private ArrayNTree<T>[] children; // exemplo do array a usar
    private int contElementos; // contador de elementos desta arvore
    private int contFilhos; // contador de filhos desta arvore
    private int capacidadeFilhos; // numero maximo de filhos

    // podem incluir outros atributos

    /////////////////////////////////////

    /**
     * Creates an empty tree 
     * @param capacity The capacity of each node, ie, the maximum number of direct successors
     */
    @SuppressWarnings("unchecked")
    public ArrayNTree(int capacity) {

        this.contElementos = 0;
        this.contFilhos = 0;
        this.capacidadeFilhos = capacity;
        this.children  = (ArrayNTree<T>[])Array.newInstance(ArrayNTree.class, capacity);
    }

    /**
     * Create a tree with one element
     * @param elem     The element value
     * @param capacity The capacity of each node, ie, the maximum number of direct successors
     */
    public ArrayNTree(T elem, int capacity) {

        this.data = elem; //miguel meteu isto
        this.contElementos = 1;
        this.children  = (ArrayNTree<T>[])Array.newInstance(ArrayNTree.class, capacity);
        this.capacidadeFilhos = capacity;
        this.contFilhos = 0;
    }

    /**
     * Creates a tree with the elements inside the given list
     * @param elem     The list with all the elements to insert
     * @param capacity The capacity of each node, ie, the maximum number of direct successors
     */
    public ArrayNTree(List<T> list, int capacity) {

        this.contElementos = 0;
        this.contFilhos = 0;
        this.capacidadeFilhos = capacity;
        this.children  = (ArrayNTree<T>[])Array.newInstance(ArrayNTree.class, capacity);

        for(T elem : list) {
            this.insert(elem);
        }
    }

    /**
     * Verifies if tree is empty
     * @best - case O(1)
     * @worst - case O(1)
     * @return true iff tree is empty
     */

    public boolean isEmpty() {
        return this.contElementos == 0;
    }



    /**
     * Verifies if tree is a leaf
     * @best - case O(1)
     * @worst - case O(1)
     * @return true iff tree is leaf
     */
    public boolean isLeaf() {
        return !this.isEmpty() && this.contFilhos == 0;
    }


    /**
     * Numero de elementos nesta arvore
     * @best - case O(1)
     * @worst - case O(1)
     * @return numero de elementos na arvore
     */
    public int size() {
        return this.contElementos;  // TODO
    }


    /**
     * Numero de folhas desta arvore
     * @best - case O(1)
     * @worst - case O(n)
     * @return Numero de folhas na arvore 
     */
    public int countLeaves() {
        if(this.isEmpty()) {
            return 0;
        } else {
            return countLeavesAux(this);
        }
    }

    private int countLeavesAux(ArrayNTree<T> tree) {
        if(tree.isLeaf()) {
            return 1;
        }

        int sumTreesLeaves = 0;

        for(int i = 0; i < tree.contFilhos; i++) {

            sumTreesLeaves += countLeavesAux(tree.children[i]);
        }

        return sumTreesLeaves;
    }

    /**
     * Altura desta arvore 
     * @best - case O(log n)
     * @worst - case O(log n) 
     * @return Altura desta arvore 
     */
    public int height() {

        if(this.isEmpty()) {
            return 0;
        } else {
            return heightAux(this);
        }
    }

    private int heightAux(ArrayNTree<T> tree) {
        if(tree.isLeaf()) {
            return 1;
        }

        ArrayNTree largestTree = tree.children[0];
        for(int i = 1; i < tree.contFilhos; i++) {
            ArrayNTree currentTree = tree.children[i];

            if(currentTree.contElementos > largestTree.contElementos) {

                largestTree = currentTree;
            }
        }

        return 1 + heightAux(largestTree);
    }

    /////////////////////////////////////

    /**
     * O menor elemento desta arvore
     * @best - case O(1)
     * @worst - case O(1)
     * @return O menor elemento desta arvore
     * @requires !this.isEmpty()
     */
    public T min() {
        return this.data;
    }

    /**
     * O maior elemento desta arvore
     * @best - case O(1)
     * @worst - case O(log n)
     * @return O maior elemento desta arvore
     * @requires !this.isEmpty()
     */
    public T max() {

        return maxAux(this);
    }


    private T maxAux(ArrayNTree<T> arvore) {

        if(arvore.isLeaf()) {

            return arvore.data;
        }

        ArrayNTree<T> ultimoFilho = arvore.children[arvore.contFilhos - 1];

        return maxAux(ultimoFilho);
    }

    /**
     * Um certo elemento estah nesta arvore?
     * @best - case O(1)
     * @worst - case O(log n)
     * @return true se o elemento existir
     *         false c. c.
     * @requires elem != null
     */
    public boolean contains(T elem) {

        if(this.isEmpty()) {

            return false;
        }

        /* como eu no metodo recursivo trabalho com o data dos filhos
         * & nao olho para o data do pai, quando chamo o metodo
         * containsAux(...) o data da 1a arvore (a root) nao eh visto
         * logo eh preciso compara-lo para ver se nao eh logo esse o elem
         */
        return elem.equals(this.data) || this.containsAux(elem, this);
    }

    /* NOTA: Para entender como o metodo funciona, eh preciso
     * perceber como a leitura prefixa funciona
     * 
     * Todos os filhos de todas arvores tem os seus filhos ordenados
     * por ordem (no vetor).
     * & o elem soh pode estar numa arvore em que o pai eh menor que
     * ele e o "irmao" seguinte eh menor que o elem
     * 
     * Exemplo:
     * elem = 6
     * Vemos os filhos do 1: 2, 5, 7 - este jah eh maior que 6 portanto
     * soh pode estar no filho anterior (o 5).
     * Vemos os filhos do 5 & lah estah o 6!
     *         1     
     *   2     5      7 
     * 3  4    6    8  9
     */
    private boolean containsAux(T elem, ArrayNTree<T> arvore) {

        if(arvore == null) {

            return false;
        }

        ArrayNTree<T> arvoreOndeProcurar = null;

        // variavel para parar o ciclo quando se encontra uma arvore pra procurar
        boolean found = false;
        for(int i = 0; i < arvore.contFilhos && !found; i++) {

            int compResult = elem.compareTo(arvore.children[i].data);

            // o elemento eh este filho?
            if(compResult == 0) {
                return true;
                // o elemento estah neste filho ou no seguinte?
            } else if(compResult > 0) {
                arvoreOndeProcurar = arvore.children[i];
                // o elemento estah no filho anterior ou n existe?
            } else { 
                found = true;
            }

        }

        return this.containsAux(elem, arvoreOndeProcurar);
    }

    /**
     * Insere um elemento nesta arvore
     * @best - case O(1)
     * @worst - case O(log2 n) 
     * @requires elem != null
     */
    public void insert(T elem) {

        if(!this.insertRoot(elem)) {

            this.insertElsewhere(elem, this);
        }
    }

    /**
     * Insere um elemento na root da arvore
     * @return true caso tenha inserido o elemento
     *         false c.c., neste caso, conclui-se que o elemento jah lah estava
     * @requires elem != null
     */
    private boolean insertRoot(T elem) {

        boolean elemIsThere = false;

        if(this.isEmpty()) {

            this.data = elem;
            this.contElementos++;

            elemIsThere = true;
        } else {

            // resultado da comparacao de elem com o data da arvore 
            int compResultData = elem.compareTo(this.data);

            // caso sejam iguais
            if(compResultData == 0) { 

                // entao jah lah estah n preciso de inserir
                elemIsThere = true;
            } else {
                // caso elem < arvore.data
                if(compResultData < 0) {

                    // substituo o data da arvore pelo elem
                    T dataCopy = this.data;

                    this.data = elem;

                    // & insiro de novo o valor antido da arvore
                    elemIsThere = this.insertElsewhere(dataCopy, this);

                    /* ^
                     @note que como o valor antido da arvore jah esteve na
                     arvore significa que nao existe jah lah esse valor
                     LOGO vou de certeza inserir algo e nao ignorar a insersao
                     por jah estar lah o valor
                     */
                }
            }


        }

        return elemIsThere;
    }

    /**
     * Insere um elemento nos filhos da arvore dada
     * @return true caso tenha inserido o elemento
     *         false c.c., neste caso, conclui-se que o elemento jah lah estava
     * @requires tree.data != null && elem != null
     */
    private boolean insertElsewhere(T elem, ArrayNTree<T> tree) {

        // por defeito, vai-se inserir na posicao 0 do vetor dos filhos da arvore
        int indexWhereToInsert = 0;

        // flags para no final do ciclo se estudar onde se vai inserir: inicio, ..., fim
        boolean foundLargertSon = false;
        boolean foundSmallerSon = false;
        for(int i = 0; i < tree.contFilhos && !foundLargertSon; i++) {

            // filho corrente a ser analisado
            ArrayNTree<T> filho = tree.children[i];

            int compResult = elem.compareTo(filho.data);

            // o elemento jah existe?
            if(compResult == 0) {
                return false;
                // elem > filho.data
            } else if(compResult > 0) {

                int compResultMax = elem.compareTo(filho.max());

                // o elem eh menor que o max do filho
                if(compResultMax < 0) {

                    // significa que tenho de inserir no filho
                    if(this.insertElsewhere(elem, filho)) {
                        tree.contElementos++;
                        return true;
                    } else {
                        return false;
                    }

                } else if(compResultMax == 0) {
                    return false;
                }

                indexWhereToInsert++;
                foundSmallerSon = true;
                // o elemento estah no filho anterior ou n existe?
            } else {
                foundLargertSon = true;
            }
        }

        // se a arvore nao pode ter + filhos *
        if(!tree.canHaveChildren()) {

            /*
           se foi encontrado um filho menor entao eh preciso 
           decrementar indexOndeInserir porque ele estah adiantado
             */
            if(foundSmallerSon) {
                indexWhereToInsert--;
            } else {

                if(this.insertReplace(elem, tree)) {
                    tree.contElementos++;
                    return true;
                } else {
                    return false;
                }
            }

            // * entao vou (re)inserir no filho eleito no for com index indexOndeInserir
            if(this.insertElsewhere(elem, tree.children[indexWhereToInsert])) {
                tree.contElementos++;
                return true;
            } else {
                return false;
            }
        }

        if(foundLargertSon) {
            // havendo algum filho maior, eh preciso abrir espaco para o filho mais pequeno
            releaseChildAtPos(indexWhereToInsert, tree);
        }

        tree.children[indexWhereToInsert] = new ArrayNTree<T>(elem, tree.capacidadeFilhos);
        tree.contElementos++;
        tree.contFilhos++;
        return true;
    }

    /**
     * Substitui um elemento no 1o filhos da arvore dada
     * E re-insere o elemento que lah estava
     * @return true caso tenha inserido o elemento
     *         false c.c., neste caso, conclui-se que o elemento jah lah estava
     * @requires tree.data != null && elem != null
     */
    private boolean insertReplace(T elem, ArrayNTree<T> tree) {

        ArrayNTree<T> firstChild = tree.children[0];

        // substituo o data da arvore pelo elem
        T dataCopy = firstChild.data;

        firstChild.data = elem;

        // & insiro de novo o valor antido da arvore
        return this.insertElsewhere(dataCopy, firstChild);
    }

    /**
     * Esta arvore ainda tem espaco para mais filhos?
     */
    private boolean canHaveChildren() {

        return this.contFilhos < this.capacidadeFilhos;
    }

    /**
     * Insere um elemento nos filhos da arvore dada
     * @return true caso tenha inserido o elemento
     *         false c.c., neste caso, conclui-se que o elemento jah lah estava
     * @requires tree.data != null && index >= 0 && index < tree.tree.contFilhos
     */
    private void releaseChildAtPos(int index, ArrayNTree<T> tree) {

        // Shiftar para a direita
        for(int j = tree.contFilhos; j > index; j--) {

            tree.children[j] = tree.children[j - 1];
        }
    }

    /**
     * Apaga um elemento da arvore
     * @best - case O(1)
     * @worst - case O(log n)
     * @requires elem != null, !isEmpty()
     */
    public void delete(T elem) {

        if(this.contains(elem)) {

            int compElemThis = elem.compareTo(this.data);

            //se o elemento for a raiz
            if(compElemThis == 0) {

                // se a raiz for 1 folha
                if(this.isLeaf()) {
                    this.data = null;
                    this.contElementos--;
                } else {
                    deleteAuxReOrder(this);
                    this.contElementos--;
                }
            } else {
                deleteAuxFindElement(this, elem);
            }
        }
    }

    /**
     * Procura o elemento a apagar
     * @requires tree != null, elem != null
     */
    private void deleteAuxFindElement(ArrayNTree<T> tree, T elem) {

        //se houver arvore
        if(tree == null) {
            return;
        }

        ArrayNTree<T> arvoreOndeApagar = null;

        for(int i = 0; i < tree.contFilhos; i++) {

            ArrayNTree<T> currentSon = tree.children[i];
            int compElemSon = elem.compareTo(currentSon.data);

            //se o elemento eh este filho
            if(compElemSon == 0) {

                if(currentSon.isLeaf()) {
                    deleteAuxOnChildren(tree, i);
                }else {
                    deleteAuxReOrder(currentSon);
                }
                this.contElementos--;
                return;
                //senao procura nos filhos
            } else {
                arvoreOndeApagar = currentSon;

            }
            deleteAuxFindElement(arvoreOndeApagar, elem);
        }
    }

    /**
     * Ordena a arvore
     * @requires tree != null
     */
    private void deleteAuxReOrder(ArrayNTree<T> tree) {

        ArrayNTree<T> firstSon = tree.children[0];

        tree.data = firstSon.data;
        if(firstSon.isLeaf()) {

            deleteAuxOnChildren(tree, 0);
        } else {

            //firstSon.contElementos--;
            deleteAuxReOrder(firstSon); 
        }
    }

    /**
     * Cria um novo array de filhos
     * @requires tree != null && index > 0 && index < tree.contFilhos
     */
    @SuppressWarnings("unchecked")
    private void deleteAuxOnChildren(ArrayNTree<T> tree, int index){

        // para o garbageCollector saber que tem de apagar o objeto antigo
        tree.children[index] = null;

        for(int i = index + 1; i < tree.contFilhos; i++) {

            tree.children[i - 1] = tree.children[i];
        }

        tree.contFilhos--;
    }

    /**
     * Is this tree equal to another object?
     * Two NTrees are equal iff they have the same values
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {

        return this == other || other instanceof ArrayNTree && equalsArrayNTree(this, (ArrayNTree<T>)other);
    }

    private boolean equalsArrayNTree(ArrayNTree<T> one, ArrayNTree<T> other) {  

        Iterator<T> it = one.iterator();
        boolean hasIt = one.contElementos == other.contElementos;

        while(it.hasNext() && hasIt) {

            hasIt = other.contains(it.next());
        }

        return hasIt;
    }

    /**
     * Passa os elementos da arvore para uma lista
     * @best - case O(1)
     * @worst - case O(n)
     * @requires !isEmpty()
     * @return Devolve uma lista com os elementos da arvore
     */
    public List<T> toList() {
        List<T> list = new ArrayList<>(this.contElementos);

        for(T elem : this) {
            list.add(elem);
        }

        return list;
    }

    /**
     * @best - case O(1)
     * @worst - case O(n)
     * @returns a new tree with the same elements of this
     */
    public ArrayNTree<T> clone() {
        if(this.isEmpty()) {

            return new ArrayNTree<>(this.capacidadeFilhos);
        }

        return cloneAux(this);
    }

    private ArrayNTree<T> cloneAux(ArrayNTree<T> tree){
        ArrayNTree<T> cloned = new ArrayNTree<>(tree.data, tree.capacidadeFilhos);

        if(tree.isLeaf()) {
            return cloned;
        }

        cloned.contFilhos = tree.contFilhos;
        cloned.contElementos = tree.contElementos;

        for(int i = 0; i < cloned.contFilhos; i++) {

            cloned.children[i] = cloneAux(tree.children[i]);
        }

        return cloned;
    }

    /**
     * Representacao textual da arvore
     * @best - case O(1)
     * @worst - case O(n) 
     * @return Devolve uma string com a representacao textual da arvore
     */
    public String toString() {
        if (isEmpty())
            return "[]";

        if (isLeaf())
            return "["+data+"]";

        StringBuilder sb = new StringBuilder();
        sb.append("["+data+":");

        for(NTree<T> brt : children)
            if (brt!=null)
                sb.append(brt.toString());

        return sb.append("]").toString();        
    }

    /** more detailed information about tree structure 
     * @best - case O(1)
     * @worst - case O(1)
     * @return Devolve uma string com informacao sobre a arvore
     */
    public String info() {
        return this + ", size: " + size() + ", height: " + height() + ", nLeaves: " + countLeaves();
    }


    /** Iterator para percorrer elementos esta arvore 
     * @best - case O(1)
     * @worst - case O(c) em que c = this.capacidadeFilhos
     * @return Iterator para percorrer elementos esta arvore 
     */
    public Iterator<T> iterator() {

        return new PrefixIterator(this);
    }

    private class PrefixIterator implements Iterator<T> {

        private Stack<ArrayNTree<T>> stack;

        private PrefixIterator(ArrayNTree<T> tree) {
            stack = new Stack<>();
            if(!tree.isEmpty()) {
                stack.push(tree);
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            ArrayNTree<T> tree = stack.peek();
            stack.pop();

            if(!tree.isLeaf()) {
                for(int i = tree.contFilhos - 1; i >= 0; i--) {
                    stack.push(tree.children[i]);
                }
            }

            return tree.data;
        }
    }
}
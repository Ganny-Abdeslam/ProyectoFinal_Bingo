package Interfaz;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import java.awt.Font;
import java.awt.Color;
import javax.swing.border.LineBorder;

import Bingo.Bingo;
import Carton.Carton;

public class Interfaz extends JFrame implements ActionListener{
    
    private JButton button_close;
    private JButton button_compra;
    private JButton button_cercanosGanar;
    private JButton button_consultar_boleto;
    private JButton button_generar_numero;
    private JTextField texto;
    private JTextField texto_boleto;
    private JTextField texto_tableros_regalar;
    private JLabel total_recaudado;
    
    ArrayList<Carton> cartonesUsuarios = new ArrayList<>();

    private ArrayList<String> bolas = new ArrayList<>();
    private ArrayList<String> bolasComparacion = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> bingo = new ArrayList<>();
    public ArrayList<ArrayList<String>> bingoAux = new ArrayList<>();
    private ArrayList<ArrayList<JLabel>> arraysLabel = new ArrayList<>();

    private boolean juego_iniciado = false;
    private boolean juego_finalizado = false;

    private int countI = 0; // Contador para recorre la matriz de bolas generadas
    private int countJ = 0; // Contador para recorre la matriz de bolas generadas
    private int countGanadores = 0; //Cnatidad de ganadores en tiempo real
    private int[] boletoDeGanadores = {-1, -1, -1}; // Posiciones de los boletos que han ganado
    private int countBoletos = 0; // Cantidad de boletos comprados
    private int countRegalados = 0; // Cantidad de boletos regalados

    //private ArrayList<Carton> cartones = new ArrayList<>();
    
    /**
     * Constructor
     * @param ancho el ancho de la ventana
     * @param alto la altura de la ventana
     */
    public void Inicializar(int ancho, int alto){
        setLayout(null);
        //Genera los parametros iniciales de la interfaz (x.inicio, y.inicio, ancho, alto)
        setBounds(0, 0, ancho, alto);
        //Hace que siempre inicie en el centro de la pantalla
        setLocationRelativeTo(null);
        setResizable(false); //No se puede modificar el tamaño de la interfaz
        setVisible(true); //Pinta la interfaz en pantalla

        //Boton de cerrado
        button_close = crearButton("Cerrar", 530, 550, 100, 30);

        //Boton de cercanos a ganar
        button_cercanosGanar = crearButton("Mostrar 3 primeros", 280, 430, 150, 30);

        //Boton de compra
        button_compra = crearButton("Comprar Boleto", 50, 550, 150, 30);

        //Boton para sacar una bola
        button_generar_numero = crearButton("Sacar Bola", 280, 550, 150, 30);

        //Fondos Recaudados
        generarLabelUtilizable("El total vendio fue: ", 510, 480).setFont(new Font("Tahoma", Font.PLAIN, 18));
        total_recaudado = generarLabelUtilizable("$0.0", 560, 510);
        total_recaudado.setFont(new Font("Tahoma", Font.PLAIN, 18));
        
        generarLabelUtilizable("Ingrese su cedula: ", 50, 480).setFont(new Font("Tahoma", Font.PLAIN, 18));;
        
        generarLabelUtilizable("Ingrese tableros regalados: ", 250, 480).setFont(new Font("Tahoma", Font.PLAIN, 18));

        //Boton de consutar un boleto en especifico
        button_consultar_boleto = crearButton("Boleto a consultar", 50, 410, 150, 30);

        texto_boleto = texto(50, 450);
        texto = texto(50, 510);
        texto_tableros_regalar = texto(280, 510);

        //Añadir un Array de Labels
        addLabelArray();

        //Generar vector d Bolas
        generarBolas();

        //Generacion de Bolas que sacan
        bolasComparacion.add("es");

        //Generación de los 70 Bingos
        bingo = Bingo.creacionBingo();
    }

    /**
     * Genera un label
     * @param mensja lo que se deasea visualizar en pantalla
     * @param x la coordenada en x en la interfaz
     * @param y la coordenada en y en la interfaz
     */
    public void generarLabel(String mensja, int x, int y){
        JLabel label = new JLabel(mensja);
        label.setBounds(x, y, 300, 30);
        add(label);
    }

    /**
     * Genera un label con return para ser utilizado a futuro
     * @param mensja lo que se deasea visualizar en pantalla
     * @param x la coordenada en x en la interfaz
     * @param y la coordenada en y en la interfaz
     * @return label Un label dentro de la interfaz para ser utilizable
     */
    public JLabel generarLabelUtilizable(String mensja, int x, int y){
        JLabel label = new JLabel(mensja);
        label.setBounds(x, y, 300, 30);
        add(label);
        return label;
    }

    /**
     * Generación de un campo de entrada tipo texto
     * @return cuadroTexto
     */
    public JTextField texto(int x, int y){
        JTextField cuadroTexto = new JTextField();
        cuadroTexto.setBounds(x, y, 150, 30);
        add(cuadroTexto);
        cuadroTexto.setText("");
        return cuadroTexto;
    }

    /**
     * Añade la matriz de arrays dentro de la interfaz, en donde se visualizarán las bolas futuramente
     */
    public void addLabelArray(){
        for(int i=0; i<9; i++){
            arraysLabel.add(addLabelArrayVector("",120+i*50));
            for(int j=0; j<9; j++){
                arraysLabel.get(i).get(j).setFont(new Font("Tahoma", Font.PLAIN, 20));
                arraysLabel.get(i).get(j).setBorder(new LineBorder(new Color(0, 0, 0), 3));
                add(arraysLabel.get(i).get(j));
            }
        }
    }

    /**
     * Añdair 9 filas de Labels para la implementación de las bolas sacadas dentro de la interfaz
     * @param mensaje El mensaje que tendra ese Label
     * @param x la posición en la que se crea
     * @return arraysLabelVector
     */
    public ArrayList<JLabel> addLabelArrayVector(String mensaje, int x){
        ArrayList<JLabel> arraysLabelVector = new ArrayList<>();

        for(int j=0; j<9; j++){
            JLabel label = new JLabel(mensaje);
            label.setBounds(x, 40+j*40, 30, 30); //En x, y, Ancho, Largo
            arraysLabelVector.add(label);
        }

        return arraysLabelVector;
    }

    /**
     * Se modifica la matriz de bolas sacadas que se imprime en la interfaz, que originalmente es totalmente vacia
     * @param msj El mensaje que se modifica dentro de la matriz, en este caso un número de la bola sacada
     * @param i la posición en i de la misma
     * @param j la posición en j de la misma
     */
    public void modificarLabelArray(String msj, int i, int j){
        arraysLabel.get(i).get(j).setText(msj);
    }

    /**
     * Generación de un boton
     * @param mensaje mensaje del boton
     * @param x coordenada en x en la interfaz
     * @param y coordenada en y en la interfaz
     * @param ancho ancho del boton
     * @param largo el largor del boton
     * @return un boton con las caracteristicas mandadas para pintarlo en la interfaz
     */
    public JButton crearButton(String mensaje, int x, int y, int ancho, int largo){
        JButton button = new JButton(mensaje);
        button.setBounds(x, y, ancho, largo);
        button.setFont(new Font("Arial", 0, 12));
        add(button);
        button.addActionListener(this); //Evento del boton
        return button;
    }

    /**
     * Genera las 75 bolas dentro de un vector para luego utilizarlo
     */
    public void generarBolas(){
        for(int i = 0; i<75; i++){
            bolas.add(i+1+"");
        }
    }

    /**
     * Busca entre las boletas compradas sí ya hay un ganador
     * @return condicion Variable que contiene true si hay un ganador o false en caso contrario
     */
    public boolean isGanador(){
        boolean condicion = false;
        boolean condicionInterna = false;

        for(int i = 0; i<bingoAux.size(); i++){
            condicionInterna = Bingo.encontrarGanador(bingoAux.get(i), bolasComparacion);
            if(condicionInterna){
                condicion = true;
                if(countGanadores < boletoDeGanadores.length && !identificarSiExiste(i)){
                    boletoDeGanadores[countGanadores] = i;
                    identificarBoleto(i);
                    countGanadores++;
                }
            }
        }

        return condicion;
    }

    /**
     * Identificar si la posicion existe dentro del vector de boletos ganadores
     * @param iterador Posicion a buscar
     * @return condicion Indica si la posicion se encuentra dentro del arreglo con un true, en caso contrario, false
     */
    private boolean identificarSiExiste(int iterador){
        boolean condicion = false;
        for(int i=0; i<boletoDeGanadores.length && !condicion; i++){
            if(boletoDeGanadores[i] == iterador){
                condicion = true;
            }
        }
        return condicion;
    }

    /**
     * Identifica si el tablero ganador es regalado o comprado
     * @param i Posicion del boleto en el arreglo de boletos en juego
     * Si la posicion ees mayor a la cantidad de boletos comprados, significa que es un tablero comprado
     */
    public void identificarBoleto(int i){
        if(i > countBoletos){
            JOptionPane.showMessageDialog(null, "Ganador del bingo fue un tablero regalado");
        }else{
            JOptionPane.showMessageDialog(null, "Ganador del bingo fue un tablero comprado");
        }
    }

    /**
     * Generación de un número aleatorio para extraerlo del vector de bolas para luego añadirlo al 
     * vector de bolasComparacion y por último removiendo ese número del vectro de bolas.
     */
    public void extraerNumeros(){
        Random aleatorio = new Random();
        int aux = 0;
        if(bolas.size()-1 != 0){
            aux = aleatorio.nextInt(bolas.size()-1);

            modificarLabelArray(bolas.get(aux), countJ, countI);
            bolasComparacion.add(bolas.get(aux));
            bolas.remove(aux);
        }else{
            bolasComparacion.add(bolas.get(0));
        }
        
    }

    /**
     * Comprueba sí ya se encontro un ganador
     */
    public void encontrarGanadores(){
        boolean ganador = false;

        ganador = isGanador();
        
        if(ganador){
            for(int i=0; i<boletoDeGanadores.length; i++){
                if(boletoDeGanadores[i] != -1){
                    JOptionPane.showMessageDialog(null, "El boleto ganador es: ");
                    generarCartonAmostrar(boletoDeGanadores[i]);
                }
            }
        }
    }

    /**
     * Genera una matriz con los vectores de los tableros comprados
     */
    private void generarBingosComprados(){
        //Bingo Auxiliar para identificar ganadores
        bingoAux.add(Bingo.generarVector(bingo.get(countBoletos)));
    }

    /**
     * Genera los tableros comprados por los usuarios, dandoles un label propio para imprimirlos en pantalla
     */
    private void generacionCartonUsuario(){
        Carton carton = new Carton();
        int aux = 0;
        cartonesUsuarios.add(carton);

        cartonesUsuarios.get(countBoletos).initialize();
        cartonesUsuarios.get(countBoletos).labelsCarton.get(cartonesUsuarios.get(countBoletos).labelsCarton.size()-1).setText(texto.getText());

        for(int i=0, j=0; i<25; i++, j++){
            
            aux = transponerMatriz(i, j, 5);

            if(j>=4){
                j = -1;
            }

            cartonesUsuarios.get(countBoletos).labelsCarton.get(aux).setText(bingoAux.get(countBoletos).get(i));
        }

        countBoletos++;
    }

    /**
     * Obtiene la posicion de un numero traspuesto de una fila o columna de una matriz se utiliza más que todo para la impresión,
     * ya que la impresión es un vector y se debe viasualizar en tipo matriz, por eso el calculo raro para una matriz 5*5
     * @param i hasta donde se desea llegar en las posiciones del vector bingo o bingoAux
     * @param j Las direcciones en j que son las cuales me llevan a una impresión adecuada. Ejemplo pasar de la posición 0 a la 5 para
     * lograr imprimir la matriz en columnas y no en filas.
     * @param escalar Aquel que se ocupa para identificar con cuanto se esta trabajando el vector o matriz y para no desbordarse
     * @return numero La posición final en la que se debe llegar a imprimir el número
     */
    public int transponerMatriz(int i, int j, int escalar){
        int numero = 0;

        if(i<escalar*1){
            numero = j*5;
        }else if(i<escalar*2){
            numero = j*5+1;
        }else if(i<escalar*3){
            numero = j*5+2;
        }else if(i<escalar*4){
            numero = j*5+3;
        }else if(i<escalar*5){
            numero = j*5+4;
        }
        
        return numero;
    }

    /**
     * Genera los tableros de los ganadores que se imprimen en pantalla
     * @param posicion del tablero ganador
     */
    public void generarCartonAmostrar(int posicion){
        Carton carton = new Carton();
        String cedula = "";
        int aux = 0;

        carton.initialize();

        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                aux = transponerMatriz(i, j, 1);
                carton.labelsCarton.get(aux).setText(bingo.get(posicion).get(i).get(j));
            }
        }

        if(posicion < countBoletos){
            cedula += cartonesUsuarios.get(posicion).labelsCarton.get(cartonesUsuarios.get(posicion).labelsCarton.size()-1).getText();
            carton.labelsCarton.get(carton.labelsCarton.size()-1).setText(cedula);
        }else{
            carton.labelsCarton.get(carton.labelsCarton.size()-1).setText("Regalado");
        }
        
        rellenarCartonesMostrados(carton);

    }

    /**
     * Rellena las posiciones dentro del tablero que se muestra en pantalla con color verde
     * Esto se realiza cuando se ha sacado una bola que coincide con un numero dentro de una matriz de un tablero
     * @param carton objeto de tipo carton que tiene un tablero especificado
     */
    private void rellenarCartonesMostrados(Carton carton){
        for(int i=0; i<countBoletos; i++){
            for(int j=0; j<carton.labelsCarton.size(); j++){
                if(bolasComparacion.indexOf(carton.labelsCarton.get(j).getText()) != -1){
                    carton.labelsCarton.get(j).setBackground(Color.GREEN);
                }
            }
        }
    }

    /**
     * Rellena las posiciones dentro de los tableros comprados por el usuario que se imperimen antes del inicio del juego
     */
    private void rellenarCartones(){
        for(int i=0; i<countBoletos; i++){
            for(int j=0; j<cartonesUsuarios.get(i).labelsCarton.size(); j++){
                if(bolasComparacion.indexOf(cartonesUsuarios.get(i).labelsCarton.get(j).getText()) != -1){
                    cartonesUsuarios.get(i).labelsCarton.get(j).setBackground(Color.GREEN);
                }
            }
        }
    }

    /**
     * Genera los tableros de bingo de los asistentes cuando el usuario indica que va a regalar una cantidad de bingos
     */
    private void generarBingosAsistentes(){
        if(!texto_tableros_regalar.getText().equals("")){
            if(countRegalados+countBoletos <= bingo.size()){
                for(int i=0; i < countRegalados; i++){
                    bingoAux.add(Bingo.generarVector(bingo.get(countBoletos+i)));
                }
            } else{
                JOptionPane.showMessageDialog(null, "Ingreso de masiados boletos solo hay: "+bingo.size());
                juego_iniciado = false;
            }
        }
    }
    
    //
    //VENTOS
    //

    /**
     * Funcion propia del sistema para genearción de eventos
     */
    public void actionPerformed(ActionEvent event){

        if(event.getSource() == button_close){
            
            System.exit(0); //Cerrar el programa

        } else if(event.getSource() == button_compra && !juego_iniciado){

            //Se comprueba sí el usuario ingreso su número de cedula para poder comprar un boleto
            if(!texto.getText().equals("") && countBoletos < 70){

                generarBingosComprados();

                generacionCartonUsuario();
            }

        } else if(event.getSource() == button_generar_numero && !juego_finalizado){

            //Activa la condición de Juego iniciado para no poder comprar más boletos
            if(!juego_iniciado){
                total_recaudado.setText("$"+countBoletos*50);

                countRegalados = Integer.parseInt(texto_tableros_regalar.getText());

                juego_iniciado = true;

                generarBingosAsistentes();
                
            }

            //Se comprueba sí el juego se inicio ya que sí se excede el número de Bingos no se puede llegar a iniciar
            if(juego_iniciado){
                extraerNumeros();
                encontrarGanadores();

                //Comprueba cuando se cumplen 3 ganadores para la finalización del juego
                if(countGanadores >= 3){
                    juego_finalizado = true;
                    JOptionPane.showMessageDialog(null, "Felicidades Shinji");
                }
            
                //Organiza la forma en la que se visualiza las bolas sacadas en la interfaz
                if(countJ >= arraysLabel.get(countI).size()-1){
                    countI++;
                    countJ = 0;
                }else{
                    countJ++;
                }
            }

            rellenarCartones();
            
        } else if (event.getSource() == button_cercanosGanar){
            //Ventanas.mostrarCercanosGanar(bingoAux);
            int [] aux = Bingo.organizarTamanios(bingoAux);
            for(int i=0; i<aux.length && i<3; i++){
                generarCartonAmostrar(aux[i]);
            }
            
            JOptionPane.showMessageDialog(null, "Estas tres ventanas emergenten son los cercanos a ganar");

            //Sí juegan menos de 3 usuarios no se pueden visualizar más cartones de los usuarios activos
            if(aux.length < 3){
                JOptionPane.showMessageDialog(null, "Actualmente hay muy pocos jugadores activos");
            }
        } else if (event.getSource() == button_consultar_boleto){

            //Identifica que el campo no este vacio para lograr buscar un boleto
            if(!texto_boleto.getText().equals("")){
                if(countBoletos+countRegalados >= Integer.parseInt(texto_boleto.getText())){
                    generarCartonAmostrar(Integer.parseInt(texto_boleto.getText())-1);
                }else{
                    int total = countBoletos+countRegalados;
                    JOptionPane.showMessageDialog(null, "Actualemte solo hay: "+total+" de judores");
                }
            }
        }
    }
}
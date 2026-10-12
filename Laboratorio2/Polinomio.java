package aed.polinomios;

import java.util.Arrays;
import java.util.function.BiFunction;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;


/**
 * Operaciones sobre polinomios de una variable con coeficientes enteros.
 */
public class Polinomio {

  // Una lista de monomios -- NO CAMBIA a private; el tester necesita acceso
  PositionList<Monomio> terms;

  /**
   * Crea un polinomio representando el valor 0.
   */
  public Polinomio() {
    terms = new NodePositionList<>();
  }

  /**
   * Crea un polinomio definado por una lista con monomios.
   * @param terms una lista de monomios
   */
  public Polinomio(PositionList<Monomio> terms) {
    this.terms = terms;
  }

  /**
   * Crea un polinomio definado por un String.
   * La representación del polinomio es una secuencia de monomios separados
   * por '+' (y posiblemente con caracteres blancos).
   * Un monomio esta compuesto por tres partes,
   * el coefficiente (un entero), el caracter 'x' (el variable), y el exponente
   * compuesto por un un caracter '^' seguido por un entero.
   * Se puede omitir multiples partes de un monomio, 
   * ejemplos:
   * <pre>
   * {@code
   * new Polinomio("2x^3 + 9");
   * new Polinomio("2x^3 + -9");
   * new Polinomio("x");   // == 1x^1
   * new Polinomio("5");   // == 5x^0
   * new Polinomio("8x");  // == 8x^1
   * new Polinomio("0");   // == 0x^0
   * }
   * </pre>
   * @throws IllegalArgumentException si el argumento es malformado
   * @param polinomio - una secuencia de monomios separados por '+'
   */
  public Polinomio(String polinomio){
      String res = ""
      int j = 0;
      int pos;
      partes = polinomio.split("+");
      if (partes[0] == "0"){
        return Polinomio(res);
      }
      for (int i = 0; partes[i] != null; i++){
          for(int j = 0; parte[i][j] != null, j++){
            if (j == " "){
              continue;
            }
            else if('0' <= j && j <= '9'){
              res[pos] = partes[i][j];
              pos++;
            }
            else if(j == "x"){
              if (partes[i][j++] == null){
                res[pos] = "^";
                pos++;
                res[pos] = "1";
                pos++;
              }
            }
            else if(j == "^"){
              res[pos] = "^";
              pos++;
            }
            j++;
          }
        i++;
      }
      return Polinomio(res)
  }

//Utilizado para suma y resta con la lógica de 1 y -1
private static Polinomio combinar(Polinomio p1, Polinomio p2, int signo){
    Polinomio res = new Polinomio();
    Position<Monomio> a = p1.terms.first();
    Position<Monomio> b = p2.terms.first();
    while (a != null || b != null){
        if (b == null || (a != null && a.element().exponente() > b.element().exponente())){
          res.terms.addLast(a.element());
          a = p1.terms.next(a);
        }
        else if (a == null || (b != null && b.element().exponente() > a .element().exponente())){
          res.terms.addLast(new Monomio(signo * b.element().coeficiente(), b.element().exponente()));
          b = p2.terms.next(b);
        }
        else{
          int new_coef = a.element().coeficiente() + signo * b.element().coeficiente();
          if (new_coef != 0){
            res.terms.addLast(new Monomio(new_coef, a.element().exponente()));
          }
          a = p1.terms.next(a);
          b = p2.terms.next(b);
        }
    }
    return res;
}


  /**
   * Suma dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la suma de los polinomios.
   */
  public static Polinomio suma(Polinomio p1, Polinomio p2) {
    return combinar(p1, p2, 1);
  }
    
  /**
   * Substraccion de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la resta de los polinomios.
   */
  public static Polinomio resta(Polinomio p1, Polinomio p2) {
    return combinar(p1, p2, -1);
  }
    
  /**
   * Calcula el producto de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return el producto de los polinomios.
   */
  public static Polinomio multiplica(Polinomio p1, Polinomio p2) {
    Polinomio res = new Polinomio();
    Position<Monomio> a = p1.terms.first();
    while(a != null){
      res= suma(res, multiplica(a.element(), p2));
      a = p1.terms.next(a);
    }
    return res;
  }

  /**
   * Calcula el producto de un monomio y un polinomio.
   * @param m el monomio
   * @param p el polinomio
   * @return el producto del monomio y el polinomio
   */
  public static Polinomio multiplica(Monomio m, Polinomio p) {
    Polinomio res = new Polinomio();
    if (m.coeficiente() == 0){
      return res;
    }
    Position<Monomio> a = p.terms.first();
    while(a != null){
      res.terms.addLast(new Monomio(m.coeficiente() * a.element().coeficiente(), m.exponente() + a.element().exponente()));
      a = p.terms.next(a);
    }
    return res;
}
    
  /**
   * Devuelve el exponente (grado) del monomio con el mayor grado
   * dentro del polinomio
   * Si el polinomio es vacio (la representacion del polinomio "0") entonces
   * el valor devuelto debe ser -1.
   * @return el grado del polinomio
   */
  public int grado() {
    if (terms.isEmpty()){
      return -1;
    }
    return terms.first().element().exponente();
  }

//Utilizado por componer para derivar pasos a esta función
private static Polinomio potencia(Polinomio p, int n){
  Polinomio res = new Polinomio();
  res.terms.addLast(new Monomio(1, 0));
  for (int i = 0; i < n; i++){
    res = multiplica(res, p);
  }
  return res;
}


  /**
   * Cambia las occurrencias del variable "x" dentro el polinomio p por el polinomio arg.
   * @return El polinomio p donde el variable "x" esta sustuido por el polinomio arg.
   * Ejemplo:
   * <pre>
   * {@code
   * Polinomio p1 = new Polinomio("2x^3 + 9");
   * Polinomio p2 = new Polinomio("x + 1");
   * Polinomio.componer(p1,p2) =>
   *   2 * (x+1)^3 + 9 = 2x^3 + 6x^2 + 6x^1 + 11
   * }
   * </pre>
   *
   */
  public static Polinomio componer(Polinomio p, Polinomio arg) {
    Polinomio res = new Polinomio();
    Position<Monomio> a = p.terms.first();
    while (a != null){
      Monomio m = a.element();
      res = suma(res, multiplica(new Monomio(m.coeficiente(), 0), potencia(arg, m.exponente())));
      a = p.terms.next(a);
    }
    return res;
  } 


// comprueba si dos polinomios son iguales
@Override
  public boolean equals(Object obj){
      if (!(obj instanceof Polinomio)){
        return false;
      }
      return terms.equals(((Polinomio) obj).terms);
  }


  /**
   * Calcula el derivado de un polinomio.
   * @return el polinomio representando el derivado.
   */
  public Polinomio derivada() {
    Position<Monomio> a = terms.first();
    Polinomio res = new Polinomio();
    while(a != null){
      Monomio m = a.element();
      if (m.exponente() > 0){
        res.terms.addLast(new Monomio(m.exponente() * m.coeficiente(), m.exponente() - 1));
      }
      a = terms.next(a);
    }
    return res;
  }

  @Override
  public int hashCode() {
    return terms.hashCode();
  }

  @Override
  public String toString() {
    if (terms.isEmpty()) return "0";
    else {
      StringBuffer buf = new StringBuffer();
      Position<Monomio> cursor = terms.first();
      while (cursor != null) {
        Monomio p = cursor.element();
        int coef = p.coeficiente();
        int exp = p.exponente();
        buf.append(new Integer(coef).toString());
        if (exp > 0) {
          buf.append("x");
          buf.append("^");
          buf.append(new Integer(exp)).toString();
        }
        cursor = terms.next(cursor);
        if (cursor != null) buf.append(" + ");
      }
      return buf.toString();
    }
  }
}

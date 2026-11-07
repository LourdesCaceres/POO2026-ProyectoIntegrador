public class Estudiante extends Socio{
    private String carrera;
    
    public Estudiante(int p_dniSocio, String p_nombre, String p_carrera){
        super(p_dniSocio, p_nombre, 20);
        this.setCarrera(p_carrera);
    }
    
    private void setCarrera(String p_carrera){
        this.carrera = p_carrera;
    }
    
    public String getCarrera(){
        return this.carrera;
    }
    
    @Override
    public String soyDeLaClase(){
       return "Estudiante"; 
    }
    
    @Override
    public boolean puedePedir(){
        //Un estudiante puede pedir si su super.puedePedir() es true (sin vencidos)
        //Y además tiene estrictamente menos de 3 libros prestados en su poder.
        return super.puedePedir() && (this.cantLibrosPrestados() < 3);
    }
    
     }

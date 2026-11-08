public class Docente extends Socio{
    private String area;
    
    public Docente(int p_dniSocio, String p_nombre, String p_area){
        super(p_dniSocio, p_nombre, 5);
        this.setArea(p_area);
    }
    
    private void setArea(String p_area){
        this.area = p_area;
    }
    
    public String getArea(){
        return this.area;
    }
    
    @Override
    public String soyDeLaClase(){
        return "Docente";
    }
    
    @Override
    public boolean puedePedir(){
        //El docente no tiene límite de cantidad de libros;
        //solo depende de qeu no tenga préstamos vencidos (lo que resuelve super.puedePedir()).
        return super.puedePedir();
    }
    
    public boolean esResponsable(){
        //Retorna true si el docente nunca tuvo ningún préstamo vencido.
        if(this.puedePedir()){
            return true;
        }
        return false;
    }
    
    public void cambiarDiasDePrestamo(int p_dias){
        //Adiciona días de préstamo al docente como premio a su responsabilidad
        this.setDiasPrestamo(this.getDiasPrestamo() + p_dias);
    }
    
    
}

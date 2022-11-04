public class Coordenada {
    private int coord[] = new int[2]; //só aceita dois valores pois eh bidimensional

    public Coordenada(int x, int y) throws Exception {
        if (x < 0 || y < 0)
            throw new Exception("Coordenada Inválida!");

        this.coord[0] = x;
        this.coord[1] = y;
    }

    public int[] getCoordenadas() {
        return this.coord;
    }

    public void setCoordenadas(int novox, int novoy) throws Exception {
        if (novox < 0 || novoy < 0)
            throw new Exception("Coordenada Invalida!");

        this.coord[0] = novox;
        this.coord[1] = novoy;
    }

    @Override
    public String toString() {
        String ret = "(";

        ret += "" + this.coord[0] + ",";
        ret += "" + this.coord[1] + ")";

        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        if (this == obj)
            return true;

        Coordenada outro = (Coordenada) obj;

        if (this.coord[0] != outro.coord[0] || this.coord[1] != outro.coord[1])
            return false;

        return true;
    }

    @Deprecated
    public int hashCode() {
        int ret = 2;

        ret = ret * 11 + new Integer(this.coord[0]);
        ret = ret * 11 + new Integer(this.coord[1]);

        return Math.abs(ret);
    }
}

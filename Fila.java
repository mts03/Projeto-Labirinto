import java.lang.reflect.*;

public class Fila <X> implements Cloneable
{
    private Object[] elemento; // private X[] elemento;
    private int      tamanhoInicial;
    private int      primeiro=0, ultimo=0, quantos=0; //vazio

    public Fila (int tamanho) throws Exception
    {
        if (tamanho<=0)
            throw new Exception ("Tamanho invalido");

        this.elemento       = new Object [tamanho]; //this.elemento=new X [tamanho];
        this.tamanhoInicial = tamanho;
    }


    private void redimensioneSe (float fator)
    {
        // X[] novo = new X [Math.round(this.elemento.length*fator)];
        Object[] novo = new Object [Math.round(this.elemento.length*fator)];
        
        int posNoThisElemento=this.primeiro, posNoNovo=0;
        for(int i=0; i<this.quantos; i++)
        {
            novo[posNoNovo]   = this.elemento[posNoThisElemento];
            posNoNovo         = (posNoNovo        +1) % novo.length;          // aumento circular
            posNoThisElemento = (posNoThisElemento+1) % this.elemento.length; // aumento circular 
        }
        
        this.elemento = novo;
        this.primeiro = 0;
        this.ultimo   = this.quantos;
    }

    private X meuCloneDeX (X x)
    {
        X ret=null;

        try
        {
            Class<?> classe         = x.getClass();
            Class<?>[] tipoDosParms = null;
            Method metodo           = classe.getMethod("clone",tipoDosParms);
            Object[] parms          = null;
            ret                     = (X)metodo.invoke(x,parms);
        }
        catch(NoSuchMethodException erro)
        {}
        catch(IllegalAccessException erro)
        {}
        catch(InvocationTargetException erro)
        {}

        return ret;
    }

    public void guardeUmItem (X x) throws Exception // FIFO
    {
        if (x==null)
            throw new Exception ("Falta o que guardar");

        if (this.quantos==this.elemento.length) // cheia
            this.redimensioneSe (2.0F);

        if (x instanceof Cloneable)
            this.elemento[this.ultimo]=meuCloneDeX(x); // x.clone();
        else
            this.elemento[this.ultimo]=x;
        /*
        if (this.ultimo<this.elemento.length-1)
            this.ultimo++;
        else
            this.ultimo=0;
        */            
        this.ultimo = (this.ultimo+1) % this.elemento.length; // aumento circular
        this.quantos++;
    }

    public X recupereUmItem () throws Exception // FIFO
    {
        if (this.quantos==0) // vazia
            throw new Exception ("Nada a recuperar");

        X ret=null;
        if (this.elemento[this.primeiro] instanceof Cloneable)
            ret = meuCloneDeX((X)this.elemento[this.primeiro]);
        else
            ret = (X)this.elemento[this.primeiro];

        return ret;
    }

    public void removaUmItem () throws Exception // FIFO
    {
        if (this.quantos==0) // vazia
            throw new Exception ("Nada a remover");

        this.elemento[this.primeiro] = null;
        this.primeiro = (this.primeiro+1) % this.elemento.length; // aumento circular

        this.quantos--;

        if (this.elemento.length>this.tamanhoInicial &&
            this.quantos<=Math.round(this.elemento.length*0.25F))
            this.redimensioneSe (0.5F);
    }

    public boolean isCheia ()
    {
        return this.quantos==this.elemento.length;
    }

    public boolean isVazia ()
    {
        return this.quantos==0;
    }

    public String toString ()
    {
        String ret = (this.quantos) + " elemento(s)";
        
        if (this.quantos!=0)
            ret += ", sendo o primeiro "+this.elemento[this.primeiro];
            
        return ret;
    }

    // equals compara this e obj
    public boolean equals (Object obj)
    {
        if(this==obj)
            return true;

        if(obj==null)
            return false;

        if(this.getClass()!=obj.getClass())
            return false;

        Fila<X> fil = (Fila<X>) obj;

        if(this.quantos!=fil.quantos)
            return false;

        if(this.tamanhoInicial!=fil.tamanhoInicial)
            return false;

        int posNoThisElemento=this.primeiro, posFilElemento=fil.primeiro;
        for(int i=0 ; i<this.quantos; i++)
        {
            if(!this.elemento[posNoThisElemento].equals(fil.elemento[posFilElemento]))
                return false;

            posNoThisElemento = (posNoThisElemento+1) % this.elemento.length; // aumento circular
            posFilElemento  = (posFilElemento +1) % fil .elemento.length; // aumento circular
        }

        return true;
    }

    @Deprecated
    public int hashCode ()
    {
        int ret=666/*qualquer positivo*/;

        ret = ret*7/*primo*/ + new Integer(this.primeiro      ).hashCode();
        ret = ret*7/*primo*/ + new Integer(this.ultimo        ).hashCode();
        ret = ret*7/*primo*/ + new Integer(this.quantos       ).hashCode();
        ret = ret*7/*primo*/ + new Integer(this.tamanhoInicial).hashCode();

        int posNoThisElemento=this.primeiro;
        for (int i=0; i<this.quantos; i++)
        {
            ret = ret*7/*primo*/ + this.elemento[posNoThisElemento].hashCode();
            posNoThisElemento = (posNoThisElemento+1) % this.elemento.length; // aumento circular
        }
        
        if (ret<0)
            ret=-ret;

        return ret;
    }

    // construtor de copia
    public Fila (Fila<X> modelo) throws Exception
    {
        if(modelo == null)
            throw new Exception("Modelo ausente");

		this.primeiro       = modelo.primeiro;
        this.ultimo         = modelo.ultimo;
        this.quantos        = modelo.quantos;
        this.tamanhoInicial = modelo.tamanhoInicial;

        //this.elemento = new X [modelo.elemento.length];
        this.elemento = new Object[modelo.elemento.length];

		int pos=modelo.primeiro;
        for(int i=0 ; i<modelo.elemento.length; i++)
        {
            this.elemento[pos] = modelo.elemento[pos];
            pos = (pos+1) % modelo.elemento.length; // aumento circular
		}
    }

    public Object clone ()
    {
        Fila<X> ret=null;

        try
        {
            ret = new Fila<X>(this);
        }
        catch(Exception erro)
        {}

        return ret;
    }
}

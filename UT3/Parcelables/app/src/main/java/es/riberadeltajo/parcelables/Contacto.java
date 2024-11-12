package es.riberadeltajo.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Contacto implements Parcelable {
    public String mNombre;
    public Date mFechaNacimiento;
    public boolean mCasado;
    public int mNumeroHijos;
    public ArrayList<Contacto> mFamiliares;

    public Contacto(String mNombre, Date mFechaNacimiento, boolean mCasado, int mNumeroHijos) {
        this.mNombre = mNombre;
        this.mFechaNacimiento = mFechaNacimiento;
        this.mCasado = mCasado;
        this.mNumeroHijos = mNumeroHijos;
        mFamiliares = new ArrayList<Contacto>();
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "mNombre='" + mNombre + '\'' +
                ", mFechaNacimiento=" + mFechaNacimiento +
                ", mCasado=" + mCasado +
                ", mNumeroHijos=" + mNumeroHijos +
                ", mFamiliares=" + mFamiliares +
                '}';
    }

    protected Contacto(Parcel in) {
        SimpleDateFormat fecha=new SimpleDateFormat("dd/mm/yyyy");
        mNombre = in.readString();
        mCasado = in.readByte() != 0;
        mNumeroHijos = in.readInt();
        try {
            mFechaNacimiento=fecha.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mFamiliares = in.createTypedArrayList(Contacto.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNombre);
        dest.writeByte((byte) (mCasado ? 1 : 0));
        dest.writeInt(mNumeroHijos);
        SimpleDateFormat fecha=new SimpleDateFormat("dd/mm/yyyy");
        dest.writeString(fecha.format(mFechaNacimiento));
        dest.writeTypedList(mFamiliares);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contacto> CREATOR = new Creator<Contacto>() {
        @Override
        public Contacto createFromParcel(Parcel in) {
            return new Contacto(in);
        }

        @Override
        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }
    };
}

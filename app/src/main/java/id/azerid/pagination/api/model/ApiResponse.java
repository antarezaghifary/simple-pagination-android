package id.azerid.pagination.api.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse{

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("rukuk")
	private String rukuk;

	@SerializedName("nama")
	private String nama;

	@SerializedName("ayat")
	private int ayat;

	@SerializedName("urut")
	private String urut;

	@SerializedName("arti")
	private String arti;

	@SerializedName("asma")
	private String asma;

	@SerializedName("audio")
	private String audio;

	@SerializedName("type")
	private String type;

	@SerializedName("nomor")
	private String nomor;

	public String getKeterangan(){
		return keterangan;
	}

	public String getRukuk(){
		return rukuk;
	}

	public String getNama(){
		return nama;
	}

	public int getAyat(){
		return ayat;
	}

	public String getUrut(){
		return urut;
	}

	public String getArti(){
		return arti;
	}

	public String getAsma(){
		return asma;
	}

	public String getAudio(){
		return audio;
	}

	public String getType(){
		return type;
	}

	public String getNomor(){
		return nomor;
	}
}
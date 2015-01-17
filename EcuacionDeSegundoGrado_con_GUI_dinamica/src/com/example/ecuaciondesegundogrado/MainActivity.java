package com.example.ecuaciondesegundogrado;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	
	private EditText eTCoef1, eTCoef2, etCoef3;  //para referencia de los EditText de los coeficientes
	private Button btnRaices, btnBorrar;  //para referencia
	
	//referencias a los views del layout a inflar(layout_raices_inflar)
	private TextView tvEcuacion;  //para referenciar el TextView del layout: layout_raices.xml
	private TextView tvRaices;
	private Button btnOK;  //para referencia el Button del layout: layout_raices.xml
	private LinearLayout layout_main;  //referencia al layout (LinearLayout) principal para
							   //inflarlo con layout_raices.xml	
	private boolean error;  //para detectar error en la entrada de los EditText
	private double a, b, c;  //coef. de la ec. tomados de los EditText
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//ubicamos los componentes
		eTCoef1 = (EditText) findViewById(R.id.eTCoef1);
		eTCoef2 = (EditText) findViewById(R.id.eTCoef2);
		etCoef3 = (EditText) findViewById(R.id.eTCoef3);
		btnRaices = (Button) findViewById(R.id.btnRaices);
		btnBorrar = (Button) findViewById(R.id.btnBorrar);
		layout_main = (LinearLayout) findViewById(R.id.linearLayout_main);
		
		//asignamos los escuchas a los botones del layout principal
		btnRaices.setOnClickListener(this);
		btnBorrar.setOnClickListener(this);
		
	}  //de onCreate()

	//============================================================//

	@Override
	public void onClick(View v) {
		
		//distinguimos si se presiono el botón "Raices" o el botón "Borrar"
		switch (v.getId()){
			case R.id.btnRaices: 
				entradaCorrecta();//detectamos si las entradas en los EditText son correctas
			    //si no hay error inflamos el layout principal con el layout "layout_raices.xml"
			    //para mostrar la ecuación, las raíces de la ec. y un botón de OK
				if (!error){
					//inflamos el layout
					View view = getLayoutInflater().inflate(R.layout.layout_raices_inflar, null);
					layout_main.addView(view);
					tvEcuacion = (TextView) findViewById(R.id.tvEcuacion);
					tvRaices = (TextView) findViewById(R.id.tvRaices);
					btnOK = (Button) findViewById(R.id.btnOK);
					btnOK.setOnClickListener(this);  //asignamos el escucha al botón
					
					calcularRaices(a, b, c);  //calculamos las raíces
					
					//deshabilitamos los EditText y botones originales hasta que
					//se presione el botón inflado "OK"
					//layout_main.setEnabled(false);
					eTCoef1.setEnabled(false);
					eTCoef2.setEnabled(false);
					etCoef3.setEnabled(false);
					btnRaices.setEnabled(false);
					btnBorrar.setEnabled(false);
				}	
			break;
			case R.id.btnOK:  //si se presiona el botón "OK" destruimos el layout inflado
				              //y habilitamos los botones y EditText originales.
				View view = layout_main.getChildAt(layout_main.getChildCount() - 1);
				layout_main.removeView(view);
				
				//en lugar de remover el layout hijo podría dejarse invisible,
				//en principio esta opción está habilitada a partir de la versión 11 del SDK
				//view.setVisibility(View.INVISIBLE);
				
				eTCoef1.setEnabled(true);  //habilitamos EditText
				eTCoef2.setEnabled(true);
				etCoef3.setEnabled(true);
				eTCoef1.setText("");  //borramos contenidos anteriores de los EditText
				eTCoef2.setText("");
				etCoef3.setText("");
				eTCoef1.requestFocus();  //le damos el foco al primer EditText (coef. "a")
				
				btnRaices.setEnabled(true);  //habilitamos botones
				btnBorrar.setEnabled(true);
			break;
		}
	}  //de OnClick()
	
	//==========================================================//
	
	//verificamos si las entradas en los EditText son correctas. Es decir son numéricas
	//y el priemer coef. es distinto de cero.
	//Mientras haya error en las entradas de los EditText mostramos un cuadro AlertDialog,
	//capturamos la excepción "NumberFormatException"
	private void entradaCorrecta(){
		error = false;  //en principio no hay error
		
		try{
			a = Double.parseDouble(eTCoef1.getText().toString());
			b = Double.parseDouble(eTCoef2.getText().toString());
			c = Double.parseDouble(etCoef3.getText().toString());
		}catch (NumberFormatException e){
			error = true;
			mostrarMensaje("Ingrese todos los coeficientes numéricos");
		}
		
		if (!error && a==0){  //verificamos que el primer coef. sea distinto de 0
			error = true;
			mostrarMensaje("El primer coeficiente debe ser distinto de cero");
			eTCoef1.requestFocus();  //le damos el foco al primer EditText (coef == 0)
		}
	}  //de entradaCorrecta();
	
	//========================================================//
	
	private void mostrarMensaje(String mensaje){
		AlertDialog.Builder cuadroAlerta;  //cuadro de alerta con botón de Aceptar
		
		//creamos el cuadro de alerta por si hay error
		cuadroAlerta = new AlertDialog.Builder(this);
		cuadroAlerta.setMessage(mensaje)
					.setTitle("Atención!!")
					.setCancelable(false)
					.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.cancel();
						}
					});
		cuadroAlerta.create();
		cuadroAlerta.show();
	}  //de mostrarMensajeError()
	
	//=================================================================//
	
	private void mostrarMensajeSalir(String mensaje){
		AlertDialog.Builder cuadroAlertaSalir; //cuadro de Alerta con botones "Si" y "No"
		
		cuadroAlertaSalir = new AlertDialog.Builder(this);
		cuadroAlertaSalir.setMessage(mensaje)
						  .setTitle("Advertencia")
						  .setCancelable(false)
						  .setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.cancel();
							}
						})
						  .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								//cerramos la aplicación
								finish();
							}
						});
		cuadroAlertaSalir.create();
		cuadroAlertaSalir.show();
	}
	
	//==================================================================//
	private void calcularRaices(double a, double b, double c){
		double discriminante;
		double raiz1, raiz2;
		DecimalFormat df = new DecimalFormat("0.000");
		String ecTexto = "";
		
		//cosntruimos el String para mostrar las ec. Verificamos si los números
		//son enteros para no mostrar punto flotante
		if (a % 1 == 0){
			int a_ = (int)a; 
			//String a_ = Double.toString(a);
			//String a_ = String.valueOf(a);
			ecTexto += a_+"x² + ";
		}else{
			ecTexto += a+"x² + ";
		}
		if (b % 1 == 0){
			int b_ = (int)b;
			ecTexto += b_+"x + ";			
		}else{
			ecTexto += b+"x + ";
		}
		if (c % 1 == 0){
			int c_ = (int)c;
			ecTexto += c_+" = 0";
		}else{
			ecTexto += c+" = 0";
		}
		
		//asignamos el String de la ec.
		tvEcuacion.setText("Ecuación: "+ecTexto);  //mostramos la ec. en el TextView inflado
		
		//calculo de las raíces de la ec.
		discriminante = b*b - 4*a*c;
		//Si las raíces son reales las calculamos. HABRÍA QUE AGREGAR LA OPCIÓN
		//DE CALCULAR RAÍCES IMAGINARIAS
		if (discriminante >= 0){
			raiz1 = (-b + Math.sqrt(discriminante))/(2*a);
			raiz2 = (-b - Math.sqrt(discriminante))/(2*a);
			
			//vemos si las raíces son enteras para mostrarlas en el formato correcto
			String raicesTexto;
			if (raiz1 % 1 == 0){
				int raiz1_ = (int)raiz1;
				raicesTexto = Integer.toString(raiz1_);
			}else{
				raicesTexto = df.format(raiz1);
			}
			raicesTexto += " y ";
			if (raiz2 % 1 == 0){
				int raiz2_ = (int)raiz2;
				raicesTexto += Integer.toString(raiz2_);
			}else{
				raicesTexto += df.format(raiz2);
			}
			
			//mostramos las raíces en el TextViewInflado
			tvRaices.setText("Raíces de la ecuación: "+raicesTexto);
		}else{
			//indicamos que la ecuación NO tiene raíces reales
			tvRaices.setText("La ecuación no tiene raíces reales");
		}
			
	}  //de calcularRaices
	
	//=====================================================================//
	
	@Override
	public void onBackPressed() {
		mostrarMensajeSalir("Está seguro que desea salir de la aplicación?");
		//super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		

}  //de la clase MainActivity

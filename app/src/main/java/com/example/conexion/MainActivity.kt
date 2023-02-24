package com.example.conexion
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.io.*
import java.net.ServerSocket
import java.net.Socket



class MainActivity : AppCompatActivity() {

    private lateinit var txtMensaje: TextView
    private lateinit var txtMensaje2: TextView
    private lateinit var btnServer: Button
    private lateinit var btnClient: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate()")
        setContentView(R.layout.activity_main)

        txtMensaje = findViewById(R.id.msgTxt)
        txtMensaje2 = findViewById(R.id.msgTxt2)
        btnServer = findViewById(R.id.button2)
        btnClient = findViewById(R.id.btn3)

        btnServer.setOnClickListener {
            ServerSocket().start()
        }

        btnClient.setOnClickListener{
                ClientSocket().start()
        }

    }

    private inner class ClientSocket: Thread() {

        @SuppressLint("SetTextI18n")
        override fun run() {

            try {
                val ip = "localhost"
                val port = 1235
                val socket = Socket(ip, port)
                val escribir = OutputStreamWriter(socket.getOutputStream())
                escribir.write("Hola servidor")
                escribir.flush()
                val leer = DataInputStream(socket.getInputStream())

                val message= leer.readUTF()
                println("mensaje recibido por el servidor: $message")


                runOnUiThread {
                    txtMensaje.text = "Mensaje del servidor:$message"
                }

                socket.close()

            } catch (ex: IOException) {
                Log.e("TAG", "Error al leer la cadena: ${ex.message}")
            }
        }
    }

    inner class ServerSocket : Thread() {

        @SuppressLint("SetTextI18n")
        override fun run() {
            val serverSocket = ServerSocket(1235)
            val clientSocket:Socket?

                try{

                   clientSocket = serverSocket.accept()

                    println("Conexion establecida desde:${clientSocket.inetAddress.hostAddress}")


                    val leer = BufferedReader(InputStreamReader(
                            clientSocket.getInputStream()))


                    val escribir = DataOutputStream(clientSocket.getOutputStream())

                    escribir.writeUTF("Hola cliente")
                    escribir.flush()
                    val message:String= leer.readLine()
                    Log.d("aparece por el amor de dios", String())

                    System.out.println("Mensaje recibido por cliente: $message")
                    escribir.flush()
                    runOnUiThread { txtMensaje2.text = "Mensaje del cliente: $message" }



                    clientSocket.close()
                }
                catch (ex:IOException){
                    Log.e("TAG", "Error al leer la cadena: ${ex.message}")
                }
            }

        }
    }




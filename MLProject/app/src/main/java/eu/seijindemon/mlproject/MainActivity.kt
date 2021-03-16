package eu.seijindemon.mlproject

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition

class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        editText = findViewById(R.id.editText)
    }

    fun selectImage(v: View)
    {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            imageView.setImageURI(data!!.data)
        }
    }

    fun startRecognizing(v: View)
    {
        if (imageView.drawable != null)
        {
            editText.setText("")
            v.isEnabled = false
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val image = InputImage.fromBitmap(bitmap, 0)
            val recognizer = TextRecognition.getClient()
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    v.isEnabled = true
                    proccessResultText(visionText)
                }
                .addOnFailureListener { e ->
                    v.isEnabled = true
                    editText.setText("Failed")
                }
        }
        else
        {
            Toast.makeText(this, "Select an Image First", Toast.LENGTH_SHORT).show()
        }
    }

    private  fun proccessResultText(resultText: Text)
    {
        if(resultText.textBlocks.size == 0)
        {
            editText.setText("No Text Found")
            return
        }
        for (block in resultText.textBlocks)
        {
            val blockText = block.text
            editText.append(blockText + "\n")
        }
    }

}
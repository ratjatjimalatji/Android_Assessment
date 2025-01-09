package com.glucode.about_you.about

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.glucode.about_you.R
import com.glucode.about_you.about.views.QuestionCardView
import com.glucode.about_you.databinding.FragmentAboutBinding
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.mockdata.MockData
import com.glucode.about_you.utils.LocalStorageManager

class AboutFragment: Fragment() {
    private lateinit var binding: FragmentAboutBinding
    private lateinit var button: Button
    private lateinit var storageManager: LocalStorageManager
    private var currentEngineerName: String? = null
    private var currentEngineer: Engineer? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handleSelectedImage(uri)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageManager = LocalStorageManager(requireContext())
        currentEngineerName = arguments?.getString("name")


        setupEngineerHeader()
        setUpQuestions()
        initializeViews(view)
        setupClickListeners()


    }

    private fun initializeViews(view: View) {
        button = view.findViewById(R.id.button)
    }

    private fun setupClickListeners() {
        button.setOnClickListener {
            showAcceptDialog()
        }

        currentEngineerName?.let { name ->
            val profilePic = storageManager.loadProfilePicture(name)
            profilePic?.let { bitmap ->
                binding.engineerHeader.profileImage.setImageBitmap(bitmap)
            }
        }
    }

    private fun setUpQuestions() {
        val engineerName = arguments?.getString("name")
        val engineer = MockData.engineers.first { it.name == engineerName }

        engineer.questions.forEach { question ->
            val questionView = QuestionCardView(requireContext())
            questionView.title = question.questionText
            questionView.answers = question.answerOptions
            questionView.selection = question.answer.index

            binding.container.addView(questionView)
        }
    }

    private fun showAcceptDialog() { //Popup dialogue for when user clicks their profile picture
        Toast.makeText(requireContext(), "Press back to exit", Toast.LENGTH_SHORT).show()

        AlertDialog.Builder(requireContext())
            .setTitle("Edit profile picture")
            .setMessage("What would you like to do with your profile picture?")
            .setNegativeButton("Delete it") { _, _ ->
                // Delete the profile picture
                deleteProfilePicture()
                Toast.makeText(
                    requireContext(),
                    "Profile picture has been deleted",
                    Toast.LENGTH_SHORT
                ).show()
            }.setPositiveButton("Update it") { _, _ ->
                // Update the profile picture
                launchImagePicker()
            }
            .setCancelable(true) //Allows users to exit process
            .show()
    }

    private fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }



    private fun setupEngineerHeader() {
        val engineerName = arguments?.getString("name")
        currentEngineerName = engineerName
        currentEngineer = MockData.engineers.first { it.name == engineerName }

        currentEngineer?.let { engineer ->
            with(binding.engineerHeader) {
                name.text = engineer.name
                role.text = engineer.role

                // First try to load saved profile picture
                val savedProfilePic = storageManager.loadProfilePicture(engineer.name)
                if (savedProfilePic != null) {
                    profileImage.setImageBitmap(savedProfilePic)
                } else {
                    //Doesnt work for now, nothing gets displayed if null
                    val resourceId = resources.getIdentifier(
                        engineer.defaultImageName,
                        "drawable",
                        requireContext().packageName
                                        )
                    if (resourceId != 0) {
                        profileImage.setImageResource(resourceId)
                    } else {
                        // If default image not found, use fallback
                        profileImage.setImageResource(R.drawable.ic_person)
                    }
                }

                // Remove tint if it's set
                profileImage.imageTintList = null

                root.isClickable = false
                root.isFocusable = false
            }
        }
    }

    private fun handleSelectedImage(uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            currentEngineerName?.let { name ->
                if (storageManager.saveProfilePicture(name, bitmap)) {
                    binding.engineerHeader.apply {
                        profileImage.setImageBitmap(bitmap)
                        // Remove any tint that might be applied
                        profileImage.imageTintList = null
                    }
                    Toast.makeText(requireContext(), "Profile picture updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to save profile picture", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error processing image", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
    private fun deleteProfilePicture() {
        currentEngineer?.let { engineer ->
            if (storageManager.deleteProfilePicture(engineer.name)) {
                // Load default image after deletion
                val resourceId = resources.getIdentifier(
                    engineer.defaultImageName,
                    "drawable",
                    requireContext().packageName
                )
                binding.engineerHeader.profileImage.apply {
                    if (resourceId != 0) {
                        setImageResource(resourceId)
                    } else {
                        setImageResource(R.drawable.ic_person)
                    }
                    // Reset tint if needed for default image
                    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
                }
                Toast.makeText(requireContext(), "Profile picture deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No profile picture to delete", Toast.LENGTH_SHORT).show()
            }
        }}}

//package com.glucode.about_you.about
//
//import android.content.Context.MODE_PRIVATE
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContentProviderCompat.requireContext
//import androidx.fragment.app.Fragment
//import com.glucode.about_you.about.views.QuestionCardView
//import com.glucode.about_you.databinding.FragmentAboutBinding
//import com.glucode.about_you.mockdata.MockData
//import java.io.IOException
//
//class AboutFragment: Fragment() {
//    private lateinit var binding: FragmentAboutBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentAboutBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setUpQuestions()
//    }
//
//    private fun setUpQuestions() {
//        val engineerName = arguments?.getString("name")
//        val engineer = MockData.engineers.first { it.name == engineerName }
//
//        engineer.questions.forEach { question ->
//            val questionView = QuestionCardView(requireContext())
//            questionView.title = question.questionText
//            questionView.answers = question.answerOptions
//            questionView.selection = question.answer.index
//
//            binding.container.addView(questionView)
//        }
//    }
//}
//
//////
//    private suspend fun loadPhotoFromInternalStorage():List<InternalStoragePhoto>{
//
//
//    }
//private fun savePhotoToInternalStorage(filename:String, bmp: Bitmap): Boolean{
//    return try{
//        //Output stream
//        openFileOutput("$filename.jpg", MODE_PRIVATE).use{ stream ->
//            if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)){
//                throw IOException("Couldn't save bitmap")
//            }
//        }
//        true
//    }catch (e: IOException){
//        e.printStackTrace()
//        false
//    }
//}
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.glucode.about_you.R
import com.glucode.about_you.about.views.QuestionCardView
import com.glucode.about_you.databinding.FragmentAboutBinding
import com.glucode.about_you.engineers.EngineersFragment
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.mockdata.MockData
import com.glucode.about_you.utils.LocalStorageManager

class AboutFragment: Fragment() {
    //Declarations
    private lateinit var binding: FragmentAboutBinding
    private lateinit var profile_image: ImageView
    private lateinit var storageManager: LocalStorageManager
    private var currentEngineerName: String? = null
    private var currentEngineer: Engineer? = null

    //Allows users to upload their pictures from gallery
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

        setupEngineerDetails()
        setUpQuestions()
        initializeViews(view)
        setupClickListeners()
    }

    private fun initializeViews(view: View) {
        profile_image = binding.profileImage
    }

    private fun setupClickListeners() {
        profile_image.setOnClickListener {
            showAcceptDialog()
        }

        currentEngineerName?.let { name ->
            val profilePic = storageManager.loadProfilePicture(name)
            profilePic?.let { bitmap ->
                binding.profileImage.setImageBitmap(bitmap) // Direct reference to ImageView
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

    //After user clicks the Engineers profile picture
    private fun showAcceptDialog() {
        Toast.makeText(requireContext(), "Press back to exit", Toast.LENGTH_SHORT).show()

        AlertDialog.Builder(requireContext())
            .setTitle("Edit profile picture")
            .setMessage("What would you like to do with your profile picture?")
            .setNegativeButton("Delete it") { _, _ ->
                deleteProfilePicture()
                Toast.makeText(
                    requireContext(),
                    "Profile picture has been deleted",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setPositiveButton("Update it") { _, _ ->
                launchImagePicker()
            }
            .setCancelable(true)
            .show()
    }

    private fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    // aligning the selected engineer's details to the data in MockData
    private fun setupEngineerDetails() {
        val engineerName = arguments?.getString("name")
        currentEngineerName = engineerName
        currentEngineer = MockData.engineers.first { it.name == engineerName }

        currentEngineer?.let { engineer ->
            with(binding) {

                // Get the current engineers info to be displayed on about frag
                name.text = engineer.name
                role.text = engineer.role
                txtYearsWorked.text = engineer.quickStats.years.toString()
                txtCoffeeCount.text = engineer.quickStats.coffees.toString()
                txtBugCount.text = engineer.quickStats.bugs.toString()

                // Load saved profile picture if there is one
                val savedProfilePic = storageManager.loadProfilePicture(engineer.name)
                if (savedProfilePic != null) {
                    profileImage.setImageBitmap(savedProfilePic) // Direct reference to ImageView
                } else {

                    // If saved image is not found, load the default image
                    val resourceId = resources.getIdentifier(
                        engineer.defaultImageName,
                        "drawable",
                        requireContext().packageName
                    )
                    if (resourceId != 0) {
                        profileImage.setImageResource(resourceId) // Direct reference to ImageView
                    } else {
                        // If default image not found, use fallback
                        profileImage.setImageResource(R.drawable.ic_person) // Fallback
                    }
                }
            }
        }
    }

    private fun handleSelectedImage(uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            currentEngineerName?.let { name ->
                if (storageManager.saveProfilePicture(name, bitmap)) {
                    binding.profileImage.setImageBitmap(bitmap)
                    binding.profileImage.imageTintList = null

                    // Notify the EngineersFragment to refresh its list
                    val engineersFragment = parentFragmentManager
                        .findFragmentByTag("EngineersFragment") as? EngineersFragment
                    engineersFragment?.refreshList()
//Messages options if image is saved or not
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
                binding.profileImage.apply {
                    if (resourceId != 0) {
                        setImageResource(resourceId) // Direct reference to ImageView
                    } else {
                        setImageResource(R.drawable.ic_person) // Fallback
                    }
                }
                //Message options if profile picture is deleted or not
                Toast.makeText(requireContext(), "Profile picture deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No profile picture to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

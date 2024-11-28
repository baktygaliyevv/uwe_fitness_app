package com.uwe.fitnessapp.ui.exercises.components

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwe.fitnessapp.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private var videoUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)

        videoUrl = arguments?.getString("video")
        if (!videoUrl.isNullOrEmpty()) {
            initializeVideoView(videoUrl!!)
        }

        return binding.root
    }

    private fun initializeVideoView(url: String) {
        val uri = Uri.parse(url)
        binding.videoView.setVideoURI(uri)
        binding.videoView.setOnPreparedListener { mp: MediaPlayer ->
            mp.isLooping = true
            binding.videoView.start()
        }

        binding.videoView.setOnErrorListener { _, _, _ ->
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.videoView.stopPlayback()
        _binding = null
    }
}

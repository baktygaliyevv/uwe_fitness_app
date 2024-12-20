package com.uwe.fitnessapp.ui.exercises.components

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.uwe.fitnessapp.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private var videoUrl: String? = null
    private var exoPlayer: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // grabbing the video url from arguments
        videoUrl = arguments?.getString("video")

        // creating an ExoPlayer instance to handle video playback
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        val playerView = binding.playerView
        playerView.player = exoPlayer

        // adding a listener to handle video size changes and errors
        exoPlayer!!.addListener(object : Player.Listener {
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                super.onVideoSizeChanged(videoSize)
                // adjusting scale based on aspect ratio to prevent stretching
                val aspectRatio = videoSize.width.toFloat() / videoSize.height.toFloat()
                if (aspectRatio > 1) {
                    playerView.scaleX = 1.5f
                    playerView.scaleY = 1.5f
                } else {
                    playerView.scaleX = 1.1f
                    playerView.scaleY = 1.1f
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                // showing a toast and navigating back if the video fails to load
                Toast.makeText(requireContext(), "Error loading video: ${error.message}", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        })

        // if we have a valid url, we set up the player and start playback
        if (!videoUrl.isNullOrEmpty()) {
            val uri = Uri.parse(videoUrl)
            val mediaItem = MediaItem.Builder().setUri(uri).build()
            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_ONE
            }
        }
    }
}

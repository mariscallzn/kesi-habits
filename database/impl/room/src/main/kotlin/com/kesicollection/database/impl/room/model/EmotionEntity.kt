package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kesicollection.core.model.Arousal
import com.kesicollection.core.model.Emotion
import com.kesicollection.core.model.Status
import com.kesicollection.core.model.Valence

/**
 * Dimensional Approach to Emotions
 * The dimensional approach to emotions suggests that emotions can be understood as points on
 * a two-dimensional space. This space is defined by two axes:
 *
 * - Valence: This axis represents the emotional polarity, ranging from positive
 * (e.g., happiness, joy) to negative (e.g., sadness, anger).
 * - Arousal: This axis represents the intensity or level of activation of the emotion, ranging from
 * low (e.g., calmness, boredom) to high (e.g., excitement, anxiety).
 *
 * [reference](https://www.researchgate.net/figure/Arousal-valence-model-of-emotions-Y-axis-represents-the-arousal-while-the-x-axis_fig1_230810302)
 * [reference_2](https://media.springernature.com/lw685/springer-static/image/art%3A10.1007%2Fs11263-019-01158-4/MediaObjects/11263_2019_1158_Fig1_HTML.png)
 */
@Entity(
    tableName = "emotions"
)
data class EmotionEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val valence: Valence,
    val arousal: Arousal,
    val status: Status,
    @ColumnInfo(name = "i_18_key")
    val i18Key: String?
)

fun Emotion.toEntity(): EmotionEntity = EmotionEntity(
    id, name, valence, arousal, status, i18Key
)

fun EmotionEntity.toEmotion(): Emotion = Emotion(id, name, valence, arousal, status, i18Key)

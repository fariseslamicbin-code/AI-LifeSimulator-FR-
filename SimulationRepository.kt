package com.lifesimulator.data

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class SimulationRepository {

    private val client = OkHttpClient()
    private val gson = Gson()

    // ⚠️ Replace with your Anthropic API key
    private val apiKey = "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"

    suspend fun runSimulation(
        profile: UserProfile,
        scenarioId: String,
        onResult: (SimulationResult) -> Unit,
        onError: (String) -> Unit
    ) {
        val targetCareer = CAREER_SCENARIOS.find { it.id == scenarioId }?.title ?: scenarioId
        val midYear = (profile.horizon.toIntOrNull() ?: 3) / 2
        val horizonYear = profile.horizon

        val prompt = """You are an AI life simulator. Given this user profile, simulate what would happen if they switched careers.

USER PROFILE:
- Age: ${profile.age}
- Current job: ${profile.currentJob}
- Current annual salary: ${'$'}${profile.salary}
- Country: ${profile.country}
- Education: ${profile.education}
- Skills: ${profile.skills.joinToString(", ").ifEmpty { "not specified" }}
- Risk tolerance: ${profile.risk}
- Time horizon: ${profile.horizon} years

TARGET CAREER: $targetCareer

Respond ONLY with a JSON object (no markdown, no backticks) with this exact structure:
{
  "verdict": "A blunt, honest 2-sentence summary. Be specific with numbers. No fluff.",
  "timeline": [
    { "year": "Now", "income": "${'$'}X", "delta": "", "note": "Starting point" },
    { "year": "Year 1", "income": "${'$'}X", "delta": "+X%", "note": "Short realistic note" },
    { "year": "Year $midYear", "income": "${'$'}X", "delta": "+X%", "note": "Short realistic note" },
    { "year": "Year $horizonYear", "income": "${'$'}X", "delta": "+X%", "note": "Short realistic note" }
  ],
  "metrics": {
    "income": { "optimistic": 85, "realistic": 62, "pessimistic": 35 },
    "stress": { "optimistic": 40, "realistic": 65, "pessimistic": 80 },
    "freeTime": { "optimistic": 70, "realistic": 50, "pessimistic": 30 },
    "growth": { "optimistic": 90, "realistic": 72, "pessimistic": 45 }
  },
  "skillGaps": ["gap1", "gap2", "gap3", "gap4"],
  "probability": {
    "optimistic": { "label": "Strong outcome", "value": "25%", "bar": 25, "color": "#34d399", "detail": "One sentence on what goes right." },
    "realistic": { "label": "Likely scenario", "value": "55%", "bar": 55, "color": "#f0b429", "detail": "One sentence on the realistic path." },
    "challenging": { "label": "Tough path", "value": "20%", "bar": 20, "color": "#f87171", "detail": "One sentence on what could go wrong." }
  }
}

All metric values are percentages (0-100). Make income numbers specific and realistic for the country and career. Be direct and honest."""

        val requestBody = JsonObject().apply {
            addProperty("model", "claude-sonnet-4-20250514")
            addProperty("max_tokens", 1000)
            add("messages", gson.toJsonTree(listOf(mapOf("role" to "user", "content" to prompt))))
        }.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.anthropic.com/v1/messages")
            .addHeader("x-api-key", apiKey)
            .addHeader("anthropic-version", "2023-06-01")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Network error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string() ?: throw Exception("Empty response")
                    val json = gson.fromJson(body, JsonObject::class.java)
                    val content = json.getAsJsonArray("content")
                        ?.firstOrNull()?.asJsonObject
                        ?.get("text")?.asString
                        ?: throw Exception("No content in response")

                    val clean = content.replace(Regex("```json|```"), "").trim()
                    val resultJson = gson.fromJson(clean, JsonObject::class.java)

                    // Parse timeline
                    val timeline = resultJson.getAsJsonArray("timeline")?.map { el ->
                        val obj = el.asJsonObject
                        TimelinePoint(
                            year = obj.get("year")?.asString ?: "",
                            income = obj.get("income")?.asString ?: "",
                            delta = obj.get("delta")?.asString ?: "",
                            note = obj.get("note")?.asString ?: ""
                        )
                    } ?: emptyList()

                    // Parse metrics
                    fun parseMetric(key: String): MetricBands {
                        val m = resultJson.getAsJsonObject("metrics")?.getAsJsonObject(key)
                        return MetricBands(
                            optimistic = m?.get("optimistic")?.asInt ?: 0,
                            realistic = m?.get("realistic")?.asInt ?: 0,
                            pessimistic = m?.get("pessimistic")?.asInt ?: 0
                        )
                    }
                    val metrics = SimMetrics(
                        income = parseMetric("income"),
                        stress = parseMetric("stress"),
                        freeTime = parseMetric("freeTime"),
                        growth = parseMetric("growth")
                    )

                    // Parse skill gaps
                    val skillGaps = resultJson.getAsJsonArray("skillGaps")?.map { it.asString } ?: emptyList()

                    // Parse probability
                    val probMap = mutableMapOf<String, ProbabilityBand>()
                    resultJson.getAsJsonObject("probability")?.entrySet()?.forEach { (k, v) ->
                        val obj = v.asJsonObject
                        probMap[k] = ProbabilityBand(
                            label = obj.get("label")?.asString ?: "",
                            value = obj.get("value")?.asString ?: "",
                            bar = obj.get("bar")?.asInt ?: 0,
                            color = obj.get("color")?.asString ?: "#f0b429",
                            detail = obj.get("detail")?.asString ?: ""
                        )
                    }

                    val result = SimulationResult(
                        verdict = resultJson.get("verdict")?.asString ?: "",
                        timeline = timeline,
                        metrics = metrics,
                        skillGaps = skillGaps,
                        probability = probMap
                    )

                    onResult(result)
                } catch (e: Exception) {
                    onError("Failed to parse simulation: ${e.message}")
                }
            }
        })
    }
}

package com.lifesimulator.data

data class UserProfile(
    val age: String = "",
    val currentJob: String = "",
    val salary: String = "",
    val country: String = "",
    val education: String = "bachelor",
    val skills: List<String> = emptyList(),
    val risk: String = "medium",
    val horizon: String = "3"
)

data class CareerScenario(
    val id: String,
    val emoji: String,
    val title: String,
    val description: String
)

val CAREER_SCENARIOS = listOf(
    CareerScenario("tech", "💻", "Software / Tech", "Developer, data scientist, product manager"),
    CareerScenario("creative", "🎨", "Creative & Design", "UX/UI, content, brand strategy"),
    CareerScenario("finance", "📊", "Finance & Consulting", "Analyst, advisor, strategy"),
    CareerScenario("entrepreneur", "🚀", "Start a Business", "Founder, freelancer, solopreneur"),
    CareerScenario("healthcare", "🏥", "Healthcare", "Clinical, admin, health tech"),
    CareerScenario("education", "📚", "Education & Research", "Teaching, academia, L&D")
)

val SKILLS_LIST = listOf(
    "Python", "JavaScript", "Data Analysis", "Project Management",
    "Sales", "Marketing", "Design", "Writing", "Finance",
    "Healthcare", "Teaching", "Engineering", "Leadership", "Research"
)

// --- Result models ---

data class TimelinePoint(
    val year: String = "",
    val income: String = "",
    val delta: String = "",
    val note: String = ""
)

data class MetricBands(
    val optimistic: Int = 0,
    val realistic: Int = 0,
    val pessimistic: Int = 0
)

data class SimMetrics(
    val income: MetricBands = MetricBands(),
    val stress: MetricBands = MetricBands(),
    val freeTime: MetricBands = MetricBands(),
    val growth: MetricBands = MetricBands()
)

data class ProbabilityBand(
    val label: String = "",
    val value: String = "",
    val bar: Int = 0,
    val color: String = "#f0b429",
    val detail: String = ""
)

data class SimulationResult(
    val verdict: String = "",
    val timeline: List<TimelinePoint> = emptyList(),
    val metrics: SimMetrics = SimMetrics(),
    val skillGaps: List<String> = emptyList(),
    val probability: Map<String, ProbabilityBand> = emptyMap()
)

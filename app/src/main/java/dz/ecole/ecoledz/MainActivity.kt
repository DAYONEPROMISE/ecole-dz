package dz.ecole.ecoledz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoleDzTheme {
                var tab by remember { mutableIntStateOf(0) }
                val destinations = listOf(
                    Triple(stringResource(R.string.tab_classes), Icons.Default.Home, stringResource(R.string.cd_classes)),
                    Triple(stringResource(R.string.tab_attendance), Icons.Default.CheckCircle, stringResource(R.string.cd_attendance)),
                    Triple(stringResource(R.string.tab_about), Icons.Default.Info, stringResource(R.string.cd_about))
                )
                Scaffold(
                    topBar = { TopAppBar(title = { Text(stringResource(R.string.app_title)) }) },
                    bottomBar = {
                        NavigationBar {
                            destinations.forEachIndexed { i, (t, icon, cd) ->
                                NavigationBarItem(
                                    selected = tab == i,
                                    onClick = { tab = i },
                                    label = { Text(t) },
                                    icon = { Icon(icon, contentDescription = cd) }
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        if (tab == 0) AddClassFab()
                    }
                ) { pad ->
                    Box(Modifier.padding(pad)) {
                        when (tab) {
                            0 -> ClassesScreen()
                            1 -> AttendanceScreen()
                            else -> AboutScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddClassFab() {
    val ctx = LocalContext.current
    val dao = (ctx.applicationContext as EcoleDzApp).db.dao()
    val scope = rememberCoroutineScope()
    var open by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    FloatingActionButton(onClick = { open = true }) { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add)) }
    if (open) AlertDialog(
        onDismissRequest = { open = false },
        title = { Text(stringResource(R.string.new_class)) },
        text = { OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(R.string.name_ex)) }, singleLine = true) },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank()) scope.launch { dao.insertClass(SchoolClass(name = name.trim())); name = ""; open = false }
            }) { Text(stringResource(R.string.add)) }
        },
        dismissButton = { TextButton(onClick = { open = false }) { Text(stringResource(R.string.cancel)) } }
    )
}

@Composable
fun ClassesScreen() {
    val ctx = LocalContext.current
    val dao = (ctx.applicationContext as EcoleDzApp).db.dao()
    val classes by dao.observeClasses().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var selected by remember { mutableStateOf<SchoolClass?>(null) }

    if (selected == null) {
        ClassListPane(classes, onOpen = { selected = it }, onDelete = { c -> scope.launch { dao.deleteClass(c) } })
    } else {
        val cls = selected!!
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val wide = maxWidth >= 840.dp
            if (wide) {
                Row(Modifier.fillMaxSize()) {
                    Box(Modifier.weight(1f)) {
                        ClassListPane(classes, onOpen = { selected = it }, onDelete = { c -> scope.launch { dao.deleteClass(c) } })
                    }
                    HorizontalDivider(Modifier.fillMaxHeight().width(1.dp))
                    Box(Modifier.weight(1f)) {
                        PupilListPane(cls, dao)
                    }
                }
            } else {
                PupilListPane(cls, dao, onBack = { selected = null })
            }
        }
    }
}

@Composable
fun EcoleDzTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val scheme = if (dark) dynamicDarkColorScheme(LocalContext.current) else dynamicLightColorScheme(LocalContext.current)
    MaterialTheme(colorScheme = scheme, content = content)
}

@Composable
fun ClassListPane(classes: List<SchoolClass>, onOpen: (SchoolClass) -> Unit, onDelete: (SchoolClass) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom).asPaddingValues()
    ) {
        items(classes) { c ->
            ElevatedCard(onClick = { onOpen(c) }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(c.name, style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = { onDelete(c) }, modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_class, c.name))
                    }
                }
            }
        }
        if (classes.isEmpty()) item {
            Text(
                stringResource(R.string.empty_classes),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun PupilListPane(cls: SchoolClass, dao: EcoleDao, onBack: (() -> Unit)? = null) {
    val scope = rememberCoroutineScope()
    val pupils by dao.observePupils(cls.id).collectAsState(initial = emptyList())
    var pupilFirst by remember { mutableStateOf("") }
    var pupilLast by remember { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize().padding(16.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            if (onBack != null) TextButton(onClick = onBack) { Text(stringResource(R.string.back_classes)) }
            else Spacer(Modifier.width(8.dp))
            Text(cls.name, style = MaterialTheme.typography.titleLarge)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = pupilLast, onValueChange = { pupilLast = it }, label = { Text(stringResource(R.string.last_name)) }, modifier = Modifier.weight(1f), singleLine = true)
            OutlinedTextField(value = pupilFirst, onValueChange = { pupilFirst = it }, label = { Text(stringResource(R.string.first_name)) }, modifier = Modifier.weight(1f), singleLine = true)
            Button(
                onClick = {
                    if (pupilLast.isNotBlank()) scope.launch {
                        dao.insertPupil(Pupil(classId = cls.id, firstName = pupilFirst.trim(), lastName = pupilLast.trim()))
                        pupilFirst = ""; pupilLast = ""
                    }
                },
                modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            ) { Text("+") }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn { items(pupils) { p -> PupilRow(p, dao) } }
    }
}

@Composable
fun PupilRow(p: Pupil, dao: EcoleDao) {
    val scope = rememberCoroutineScope()
    val grades by dao.observeGrades(p.id).collectAsState(initial = emptyList())
    var showAdd by remember { mutableStateOf(false) }
    var subject by remember { mutableStateOf("Maths") }
    var value by remember { mutableStateOf("") }
    val bySubject = remember(grades) { grades.groupBy { it.subject }.mapValues { e -> e.value.map { it.value } } }
    val coefs = remember(grades) { grades.groupBy { it.subject }.mapValues { e -> e.value.firstOrNull()?.coefficient ?: 1.0 } }
    val avg = remember(bySubject, coefs) { GradeCalculator.generalAverage(bySubject, coefs) }

    ElevatedCard(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${p.lastName} ${p.firstName}", style = MaterialTheme.typography.titleSmall)
                Row {
                    TextButton(onClick = { showAdd = !showAdd }) { Text(if (showAdd) stringResource(R.string.close) else stringResource(R.string.add_grade)) }
                    IconButton(onClick = { scope.launch { dao.deletePupil(p) } }) { Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete)) }
                }
            }
            Text(
                if (avg == null) stringResource(R.string.avg_none)
                else stringResource(R.string.avg_fmt, "%.2f".format(avg), GradeCalculator.mention(avg)),
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary
            )
            grades.forEach { g ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${g.subject} (${g.term}): ${g.value}/20")
                    TextButton(onClick = { scope.launch { dao.deleteGrade(g.id) } }) { Text("x") }
                }
            }
            if (showAdd) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text(stringResource(R.string.subject)) }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = value, onValueChange = { value = it }, label = { Text("/20") }, modifier = Modifier.weight(1f), singleLine = true)
                    Button(onClick = {
                        val v = value.replace(',', '.').toDoubleOrNull()
                        if (v != null && GradeCalculator.isValidGrade(v) && subject.isNotBlank())
                            scope.launch { dao.insertGrade(Grade(pupilId = p.id, subject = subject.trim(), value = v)); value = "" }
                    }) { Text("OK") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen() {
    val ctx = LocalContext.current
    val dao = (ctx.applicationContext as EcoleDzApp).db.dao()
    val classes by dao.observeClasses().collectAsState(initial = emptyList())
    var classId by remember { mutableStateOf<Long?>(null) }
    val scope = rememberCoroutineScope()
    val today = remember { java.time.LocalDate.now().toString() }
    val pupils = if (classId == null) emptyList() else dao.observePupils(classId!!).collectAsState(initial = emptyList()).value
    var marks by remember { mutableStateOf(mapOf<Long, String>()) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.attendance, today), style = MaterialTheme.typography.titleLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            classes.forEach { c ->
                FilterChip(selected = classId == c.id, onClick = { classId = c.id; marks = emptyMap() }, label = { Text(c.name) })
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(pupils) { p ->
                val cur = marks[p.id] ?: "PRESENT"
                ElevatedCard(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("${p.lastName} ${p.firstName}")
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("PRESENT" to stringResource(R.string.st_present), "ABSENT" to stringResource(R.string.st_absent), "LATE" to stringResource(R.string.st_late), "EXCUSED" to stringResource(R.string.st_excused)).forEach { (code, label) ->
                                FilterChip(selected = cur == code, onClick = {
                                    marks = marks + (p.id to code)
                                    scope.launch { dao.markAttendance(Attendance(pupilId = p.id, date = today, status = code)) }
                                }, label = { Text(label) })
                            }
                        }
                    }
                }
            }
        }
        if (classId == null) Text(stringResource(R.string.choose_class))
    }
}

@Composable
fun AboutScreen() {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("EcoleDZ v1.0.0", style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.about_1))
        Text(stringResource(R.string.about_2))
        Text(stringResource(R.string.about_3))
        Text(stringResource(R.string.about_4))
        Text(stringResource(R.string.about_5))
    }
}

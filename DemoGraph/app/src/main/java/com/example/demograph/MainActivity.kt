package com.example.demograph

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Screens {
    INPUTSCREEN,
    GRAPHSCREEN,
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val xCoordinates = remember { mutableStateListOf<Int>() }
            val yCoordinates = remember { mutableStateListOf<Int>() }
            val navController = rememberNavController()

            NavHost(navController, startDestination = Screens.INPUTSCREEN.name) {
                composable(Screens.INPUTSCREEN.name) {
                    UserInput(yCoordinates, xCoordinates, navController)
                }
                composable(Screens.GRAPHSCREEN.name) {
                    ComplexGraphUI(yCoordinates, xCoordinates, 12, 300.0, navController)
                }
            }
        }
    }
}

/*
 * Description: Allows a user to input coordinates to send to ComplexGraphUI to plot a graph.
 * Parameters: yCoordinates (a remember mutable list of Ints that holds the y coordinates),
 * xCoordinates (a remember mutable list of Ints that holds the x coordinates),
 * navController: (a rememberNavController that allows the user to plot the graph)
 */
@Composable
fun UserInput(
    yCoordinates: SnapshotStateList<Int>,
    xCoordinates: SnapshotStateList<Int>,
    navController: NavHostController
){
    val yPlot = remember { mutableStateListOf("", "", "", "", "", "", "", "", "", "", "", "") }
    val xPlot = remember { mutableStateListOf("", "", "", "", "", "", "", "", "", "", "", "") }
    val current = LocalContext.current
    val inputErrorLengthDiff = "The X and Y inputs need to be the same length"
    val inputErrorToSmall = "The X and Y axis needs at least 2 inputs in each field"

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Y-Axis Concordances",
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            CoordinateTextBox(yPlot, 0)
            CoordinateTextBox(yPlot, 1)
            CoordinateTextBox(yPlot, 2)
            CoordinateTextBox(yPlot, 3)
            CoordinateTextBox(yPlot, 4)
            CoordinateTextBox(yPlot, 5)
        }

        Row(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        ) {
            CoordinateTextBox(yPlot, 6)
            CoordinateTextBox(yPlot, 7)
            CoordinateTextBox(yPlot, 8)
            CoordinateTextBox(yPlot, 9)
            CoordinateTextBox(yPlot, 10)
            CoordinateTextBox(yPlot, 11)
        }

        Text(
            text = "X-Axis Concordances",
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            CoordinateTextBox(xPlot, 0)
            CoordinateTextBox(xPlot, 1)
            CoordinateTextBox(xPlot, 2)
            CoordinateTextBox(xPlot, 3)
            CoordinateTextBox(xPlot, 4)
            CoordinateTextBox(xPlot, 5)
        }

        Row(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            CoordinateTextBox(xPlot, 6)
            CoordinateTextBox(xPlot, 7)
            CoordinateTextBox(xPlot, 8)
            CoordinateTextBox(xPlot, 9)
            CoordinateTextBox(xPlot, 10)
            CoordinateTextBox(xPlot, 11)
        }

        OutlinedButton(
            onClick = {
                stringToInt(yCoordinates, xCoordinates, yPlot, xPlot)
                if (yCoordinates.size < 2 || xCoordinates.size < 2) {
                    Toast.makeText(current, inputErrorToSmall, Toast.LENGTH_SHORT).show()
                } else if (yCoordinates.size == xCoordinates.size) {
                    navController.navigate(Screens.GRAPHSCREEN.name)
                } else {
                    Toast.makeText(current, inputErrorLengthDiff, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(top = 25.dp).height(50.dp).width(200.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(2.dp, Color.Black),
            colors = ButtonColors(
                contentColor = Color.Black,
                containerColor = Color.White,
                disabledContentColor = Color.Black,
                disabledContainerColor = Color.White
            )
        ) {
            Text("Plot Graph", fontSize = 20.sp)
        }
    }
}

/*
 * Description: Converts remember mutable state list of string to a remember mutable state list of
 * int.
 * Parameters: yCoordinates (a remember mutable list of Ints that holds the y coordinates),
 * xCoordinates (a remember mutable list of Ints that holds the x coordinates),
 * yPlot (a remember mutable list of Strings that holds the y coordinates),
 * xPlot (a remember mutable list of Strings that holds the x coordinates),
 */
fun stringToInt(
    yCoordinates: SnapshotStateList<Int>,
    xCoordinates: SnapshotStateList<Int>,
    yPlot: SnapshotStateList<String>,
    xPlot: SnapshotStateList<String>
) {
    yCoordinates.clear()
    xCoordinates.clear()

    for (i in 0 until yPlot.size) {
        if (yPlot[i] != "") {
            yCoordinates.add(yPlot[i].toInt())
        }
    }

    for (i in 0 until xPlot.size) {
        if (xPlot[i] != "") {
            xCoordinates.add(xPlot[i].toInt())
        }
    }
}

/*
 * Description: Custom outline text box for user input one coordinate
 * Parameters: coordinateHolder (a remember mutable list of Strings that holds coordinates),
 * index (an int to put coordinate at the right place in the list)
 */
@Composable
fun CoordinateTextBox(coordinateHolder: SnapshotStateList<String>, index: Int) {
    OutlinedTextField(
        value = coordinateHolder[index],
        onValueChange = { coordinateHolder[index] = it },
        modifier = Modifier.size(65.dp, 50.dp).padding(start = 3.dp, end = 3.dp)
    )
}

/*
 * Description: Plots a graph based on user inputted coordinates
 * Parameters: lstOfUnitsColumn (list of ints for the y axis coordinates),
 * lstOfUnitsRow (list of ints for the x axis coordinates),
 * gridSize (a int which determines the amount of points in a graph),
 * graphSize (a double to determines the actual size of the graph on screen),
 * navController: (a rememberNavController that allows the user to go back to main screen)
 */
@Composable
fun ComplexGraphUI(
    lstOfUnitsColumn: List<Int>,
    lstOfUnitsRow: List<Int>,
    gridSize: Int,
    graphSize: Double,
    navController: NavHostController
) {
    val labelSpacing = graphSize/gridSize

    val srtdLstofUnitsColumn = lstOfUnitsColumn.sorted()
    val columnIncrementAmount = NumberIncremented(lstOfUnitsColumn, srtdLstofUnitsColumn)
    val columLabelList = unitToAxisLabel(srtdLstofUnitsColumn, columnIncrementAmount, gridSize)
    val lstUnitsInGraphFormat = unitToGridComplex(lstOfUnitsColumn, columLabelList)

    val srtdLstOfUnitsRow = lstOfUnitsRow.sorted()
    val rowIncrementAmount = NumberIncremented(lstOfUnitsRow, srtdLstOfUnitsRow)
    val rowLabelList = unitToAxisLabel(srtdLstOfUnitsRow, rowIncrementAmount, gridSize)
    val lstUnitsRowInGraphFormat = unitToGridComplex(lstOfUnitsRow, rowLabelList)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier.align(Alignment.TopStart)
        ) {
            Image(
                painter = painterResource(R.drawable.arrow_ios_back),
                "Back arrow",
                modifier = Modifier.size(50.dp).clickable {
                    navController.popBackStack()
                }
            )
        }

        Row(
            Modifier.align(Alignment.Center)
        ) {

            /* Prints labels for Y-axis */
            Column(
                Modifier.height(graphSize.dp),
                verticalArrangement = Arrangement.Center
            ) {
                for (i in 0 until columLabelList.size) {
                    Box(
                        Modifier.height(labelSpacing.dp)
                    ) {
                        Text(
                            text = "${columLabelList.get((columLabelList.size-1)-i)}",
                            modifier = Modifier.align(Alignment.BottomCenter).padding(end = 4.dp),
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Column (

            ) {
                Canvas(modifier = Modifier.size(graphSize.dp)) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val numOfGrids = gridSize
                    val yAxisGridLine = canvasHeight/numOfGrids
                    val xAxisGridLine = canvasWidth/numOfGrids
                    var lineCoordinates: Float
                    val xCoordinates = mutableListOf<Float>()
                    val yCoordinates = mutableListOf<Float>()

                    // when generating grid only does 1 through numOfGrids so need to add 0 here
                    xCoordinates.add(0f)

                    /* -------- Draw graph boarder: X-axis -------- */
                    drawLine(
                        start = Offset(x = 0f, y = canvasHeight),
                        end = Offset(x = canvasWidth, y = canvasHeight),
                        color = Color.Black,
                        strokeWidth = 10f
                    )
                    /* -------- Draw graph boarder: Y-axis -------- */
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = canvasHeight),
                        color = Color.Black,
                        strokeWidth = 10f
                    )

                    /* -------- Draw grid cells: Y-axis (note, Y-axis is inverted) -------- */
                    for (i in 1 until numOfGrids){
                        lineCoordinates = yAxisGridLine*i
                        yCoordinates.add(lineCoordinates)
                        drawLine(
                            start = Offset(x = lineCoordinates, y = 0f),
                            end = Offset(x = lineCoordinates, y = canvasHeight),
                            color = Color.LightGray,
                            strokeWidth = 5f
                        )
                    }
                    /* Since Y-axis is inverted this is effective adding the 0 like in the
                       X-axis above */
                    lineCoordinates = yAxisGridLine*numOfGrids
                    yCoordinates.add(lineCoordinates)

                    /* -------- Draw grid cells: X-axis -------- */
                    for (i in 1 until numOfGrids){
                        lineCoordinates = xAxisGridLine*i
                        xCoordinates.add(lineCoordinates)
                        drawLine(
                            start = Offset(x = canvasWidth, y = lineCoordinates ),
                            end = Offset(x = 0f, y = lineCoordinates),
                            color = Color.LightGray,
                            strokeWidth = 5f
                        )
                    }

                    /* -------- Draw plot line -------- */
                    // used to get y coordinate placement since yCoordinates is inverted
                    val yCoordinatesSize = yCoordinates.size-1
                    val currentIndexYCoordinate = yCoordinates.get(0)
                    val smallestValInYAxisList = srtdLstofUnitsColumn.get(0)
                    // if graph is counting by 6 it would take 6 preciseCoordinatePointYAxis to reach the next Y-axis label
                    val preciseCoordinatePointYAxis = currentIndexYCoordinate/(columnIncrementAmount)
                    val currentIndexXCoordinate: Float

                    if (xCoordinates.size < 2) {
                        currentIndexXCoordinate = xCoordinates.get(0)
                    } else {
                        currentIndexXCoordinate = xCoordinates.get(1)
                    }

                    val smallestValInXAxisList = srtdLstOfUnitsRow.get(0)
                    val preciseCoordinatePointXAxis = currentIndexXCoordinate/(rowIncrementAmount)

                    for (i in 0 until lstUnitsInGraphFormat.size-1) {
                        val currentYCoordinateIndex = yCoordinatesSize-lstUnitsInGraphFormat.get(i)
                        val currentYCoordinateNextIndex = yCoordinatesSize-lstUnitsInGraphFormat.get(i+1)
                        val currentXCoordinateIndex = lstUnitsRowInGraphFormat.get(i)
                        val currentXCoordinateNextIndex = lstUnitsRowInGraphFormat.get(i+1)

                        // gets coordinate placement between Y-axis column labels
                        // done by getting remainder of value from label val minus smallest val
                        // then getting the amount of precise coordinate points for specific index
                        var remainder: Int
                        var remainderNextIndex: Int

                        if (columnIncrementAmount == 0) {
                            remainder = 0
                            remainderNextIndex = 0
                        } else {
                            remainder = (lstOfUnitsColumn.get(i) - smallestValInYAxisList) % columnIncrementAmount
                            remainderNextIndex = (lstOfUnitsColumn.get(i+1) - smallestValInYAxisList) % columnIncrementAmount
                        }

                        var columnPreciseCoordinate = (preciseCoordinatePointYAxis)*((columnIncrementAmount)-remainder)
                        var columPreciseCoordinateNextIndex = (preciseCoordinatePointYAxis)*((columnIncrementAmount)-remainderNextIndex)

                        if (rowIncrementAmount == 0) {
                            remainder = 0
                            remainderNextIndex = 0
                        } else {
                            remainder = (lstOfUnitsRow.get(i) - smallestValInXAxisList) % rowIncrementAmount
                            remainderNextIndex = (lstOfUnitsRow.get(i+1) - smallestValInXAxisList) % rowIncrementAmount
                        }

                        var rowPreciseCoordinate = (preciseCoordinatePointXAxis)*((rowIncrementAmount)-remainder)
                        var rowPreciseCoordinateNextIndex = (preciseCoordinatePointXAxis)*((rowIncrementAmount)-remainderNextIndex)

                        // if remainder is zero sets the preciseCoordinate its current label instead of the next one
                        if (yCoordinates.maxOf {it} == yCoordinates.get(currentYCoordinateIndex)) {
                            columnPreciseCoordinate = 0f
                        }
                        if (yCoordinates.maxOf {it} == yCoordinates.get(currentYCoordinateNextIndex)) {
                            columPreciseCoordinateNextIndex = 0f
                        }
                        if (yCoordinates.minOf {it} == yCoordinates.get(currentYCoordinateIndex)) {
                            columnPreciseCoordinate = 0f
                        }
                        if (yCoordinates.minOf {it} == yCoordinates.get(currentYCoordinateNextIndex)) {
                            columPreciseCoordinateNextIndex = 0f
                        }

                        if (xCoordinates.maxOf {it} == xCoordinates.get(currentXCoordinateIndex)) {
                            rowPreciseCoordinate = 0f
                        }
                        if (xCoordinates.maxOf {it} == xCoordinates.get(currentXCoordinateNextIndex)) {
                            rowPreciseCoordinateNextIndex = 0f
                        }
                        if (xCoordinates.minOf {it} == xCoordinates.get(currentXCoordinateIndex)) {
                            rowPreciseCoordinate = 0f
                        }
                        if (xCoordinates.minOf {it} == xCoordinates.get(currentXCoordinateNextIndex)) {
                            rowPreciseCoordinateNextIndex = 0f
                        }

                        drawLine(
                            start = Offset(
                                x = xCoordinates.get(currentXCoordinateIndex) - rowPreciseCoordinate,
                                y = yCoordinates.get(currentYCoordinateIndex) + columnPreciseCoordinate
                            ),
                            end = Offset(
                                x = xCoordinates.get(currentXCoordinateNextIndex) - rowPreciseCoordinateNextIndex,
                                y = yCoordinates.get(currentYCoordinateNextIndex) + columPreciseCoordinateNextIndex
                            ),
                            color = Color.Red,
                            strokeWidth = 5f
                        )
                    }
                }

                /* Prints labels for X-axis */
                Row(
                    modifier = Modifier.width(graphSize.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 0 until rowLabelList.size) {
                        Box(
                            Modifier.width(labelSpacing.dp)
                        ) {
                            Text(
                                text = "${rowLabelList.get(i)}",
                                modifier = Modifier.align(Alignment.CenterStart).rotate(75f).padding(top = 13.dp),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

        }
    }
}

/*
 *  Description: gets a list of the nearest label value for unit value of graph.
 *  When unit value is added to list it must be less than label value.
 * Parameters: unitList (list of int to plot),
 * labelList (list of graph labels)
 */
fun unitToGridComplex(unitList: List<Int>, labelList: List<Int>): List<Int> {
    val unitGridFormat =  mutableListOf<Int>()
    val labelLst = labelList.toSet().toMutableList()

    for (i in 0 until unitList.size) {
        for (j in 0 until labelLst.size) {
            if (unitList.get(i) == labelLst.get(0)){
                unitGridFormat.add(j)
                break;
            } else if (unitList.get(i) < labelLst.get(j)) {
                unitGridFormat.add(j)
                break;
            } else if (unitList.get(i) == labelLst.get(labelLst.size-1)) {
                unitGridFormat.add(labelLst.size-1)
                break;
            }
        }
    }
    return unitGridFormat
}

/*
 * Description: returns what the column label should increment by based on
 * a range data that does not increment evenly.
 * example: the Y-axis label goes 0, 2, 4, 6
 * Parameters: unitList (List of ints),
 * sortedUnits (sorted list of unitList)
 */
fun NumberIncremented(unitList: List<Int>, sortedUnits: List<Int>): Int {
    var maxMinDiff = sortedUnits.get(sortedUnits.size-1) - sortedUnits.get(0)

    for (i in 1 until unitList.size) {
        if (maxMinDiff % (unitList.size-1) != 0) {
            maxMinDiff++
        }
    }

    return maxMinDiff/(unitList.size-1)
}

/*
 * Description: creates the labels for the X and Y axis graph based on
 * incremented value.
 * Parameters: sortedUnits (sorted list of unitList),
 * numbIncrement (the increment value of graph axis labels),
 * gridSize (a int which determines the amount of points in a graph)
 */
fun unitToAxisLabel(sortedUnits: List<Int>, numbIncrement: Int, gridSize: Int): List<Int> {
    val unitColumnFormat =  mutableListOf<Int>()
    var numInc = numbIncrement

    unitColumnFormat.add(sortedUnits.get(0))
    if (numbIncrement == 0) {
        numInc = 1
    }
    val idx = sortedUnits.get(0)
    for (i in 1 until gridSize) {
        unitColumnFormat.add(idx+(numInc*i))
    }

    return unitColumnFormat
}







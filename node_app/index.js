//Import Express
var express = require('express');
var app = express();

//Import mongoose
var mongoose = require('mongoose');

//Assign the Atlas API String
//If you want to connect to an existing/new database, 
//replace mongodb.net/test with mongodb.net/your_preferred_db_name                 ------
var url = "mongodb+srv://SuperUser:SuperUserPassword@cis350group6-m0ube.mongodb.net/DBLeah";

//Make a connection to the Atlas cluster
mongoose.connect(url, {
	useNewUrlParser: true,
	useUnifiedTopology: true
});

//See if connection was successful
mongoose.connection.on('connected', () =>{
	console.log("Mongoose is connected");
});


//*************************************************************************************
// SCHEMAS
//*************************************************************************************

//Creates schema for new TestCollection
//Make sure collection parameter and mongoose.model's first argument match
var Schema = mongoose.Schema;
const userSchema = new Schema({
		username: {type: String, required: true, unique: true}, 
		firstName: String,
		lastName: String,
		password: String,
		gender: String,
		email: String,
		dob: Date,
		adminStatus: Boolean,
		muted: Boolean,
		followers: Array,
		following: Array,
		habits: Array
	},{
		collection : "Users"
	});
const userConstructor = mongoose.model('Users', userSchema);

const habitSchema = new Schema({
		user: String,
		category: String,
		goal: String,
		timesPerWeekGoal: Number,
		timesThisWeek: Number,
		lastUpdated: Date,
		priority: Number
	},{
		collection : "Habits"
	});
const habitConstructor = mongoose.model('Habits', habitSchema);

//*************************************************************************************
//*************************************************************************************

app.use('/createHabit', (req, res) => {
	var goal = req.query.goal.split("-").join(" ");
	console.log(goal);

	const data = {
		user: req.query.user,
		category: req.query.category,
		goal: goal,
		timesPerWeekGoal: req.query.timesPerWeekGoal,
		timesThisWeek: 0,
		lastUpdated: new Date().getTime(),
		priority: req.query.priority
	}

	const entry = new habitConstructor(data);
	entry.save((error) => {
		if (error) {
			console.log("error adding habit to database");
			console.log(error);
		} else {
			console.log(data);
			console.log("habit successfully added to database");
			res.send("success");
		}
	})
});

//*************************************************************************************
//*************************************************************************************

app.use('/getUser', (req, res) => {
	var user = req.query.user;
	userConstructor.findOne({username : user},
		'username firstName lastName password gender email dob adminStatus muted followers following habits',
		function(err, entry) {
			if (err) {
				console.log("error querying user");
				console.log(err);
			} else {
				console.log(entry);
				res.send(entry);
			}
		})

})


app.use('/getHabits', (req, res) => {
	var user = req.query.user;

	habitConstructor.find({user : user},
		'user category goal timesPerWeekGoal timesThisWeek lastUpdated priority',
		function(err, entries) {
			if (err){
				console.log("error querying habits");
				console.log(err);
			} else {
				console.log(entries);
				res.send(entries);
			}			
		})
})

http://localhost:3000/updateHabit/get?id=5e9a54061c9d4400006d559c&category=fitness&goal=my-goal&timesPerWeekGoal=7&timesThisWeek=8&priority=5

//*************************************************************************************
//*************************************************************************************

app.use('/updateHabit', (req, res) => {
	var goal = req.query.goal.split("-").join(" ");
	console.log(goal);

	var condition = {_id: mongoose.Types.ObjectId(req.query.id)};
	var update = {
		category: req.query.category,
		goal: goal,
		timesPerWeekGoal: req.query.timesPerWeekGoal,
		timesThisWeek: req.query.timesThisWeek,
		lastUpdated: new Date().getTime(),
		priority: req.query.priority
	}
	
	habitConstructor.update(condition, update, function (err, entry) {
		if (err) {
			console.log("error updating habit");
			console.log(err);
		} else {
			console.log(update);
			console.log("habit successfully updated");
			res.send("success");
		}
	})
})

//*************************************************************************************
//*************************************************************************************

// This starts the web server on port 3000. 
// This just sends back a message for any URL path not covered above
app.use('/', (req, res) => {
    console.log('Default message.');
    res.send('Default message.');
    return
});

app.listen(3000, () => {
    console.log('Listening on port 3000');
    return
});
import mysql.connector
from flask import Flask, jsonify, request
from flask_cors import CORS
mydb = mysql.connector.connect(
  host="localhost",
  user="root",
  password="",
  database="map_innovative_user"
)


#mycursor = mydb.cursor()
#x=request.forms.get('name')

#val = ('john',)
#sql = "INSERT INTO subject (name) VALUES (%s)"


#mycursor.execute(sql, val)

#mydb.commit()
#print(mycursor.rowcount, "record inserted.")

app=Flask(__name__)
CORS(app)
@app.route('/',methods=['POST'])
def function():
	x=request.args.get('email')
	y=request.args.get('pass')
	mycursor = mydb.cursor()
	
	val = (x,y,)
	sql = "SELECT * FROM `user` WHERE Email=(%s) AND Password=(%s)"

	mycursor.execute(sql, val)
	myresult = mycursor.fetchall()

	#mydb.commit()
	return jsonify({"count":len(myresult)}) 


app.run(debug=True)	

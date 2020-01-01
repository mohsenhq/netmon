import requests
import json

payload = {
"groupid": "375",
"username": "itccenter",
"password": "center@itc+0514*GbYe",
"bankid":"1",
"callbackurl":"CRM.ICTC.Sharif.edu",
"id2":"12",
"nc":"0569932531",
"name":"esmaeil",
"family":"mollaahmadi",
"tel": "02166535215",
"mobile": "09338880913",
"email": "e_mollaahmadi3@modernisc.com",
"amount":"1000"

}

headers = {'content-type': 'application/json'}

url = 'http://payment.sharif.ir//research/ws.asmx/Request'
r = requests.post(url,json.dumps(payload),headers=headers )
print (r.content)

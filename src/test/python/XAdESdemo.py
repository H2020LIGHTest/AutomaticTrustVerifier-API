import requests
from base64 import b64encode

#BASE = 'http://localhost:8080'
#BASE = 'http://localhost/atvapi'
#BASE = 'https://lightest-dev.iaik.tugraz.at/atvapi'
BASE = 'https://atvapi.tug.do.nlnetlabs.nl/atvapi/'


POLICY_FILE      = '../policies/policy_xades1.tpl'
TRANSACTION_FILE = '../testdata/example.xml'


if requests.get(BASE + '/api/v1/').json()['result'] != 1:
    print('Wrong BASE?')


payload = dict()
payload['policy']      = b64encode(open(POLICY_FILE, 'rb').read()).decode('utf-8')
payload['transaction'] = b64encode(open(TRANSACTION_FILE, 'rb').read()).decode('utf-8')

#print(payload)

resp = requests.post(BASE + '/api/v1/addInstance', json=payload)

print(resp.text)

for line in resp.json()['report']:
    print(line)

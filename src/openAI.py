import os
import openai
import sys

my_key = ""
openai.api_key = (my_key)

response = openai.Completion.create(model="text-davinci-003", prompt=sys.argv[1], temperature=0.7, max_tokens=1200)
text = response['choices'][0]['text']
print(text)

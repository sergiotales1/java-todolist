## Some things i've learnt through the journey:
serialization = transform data into bytes
deserialization = oposite ^^

data is broken into packets, arrives the server that collects packet by packet and unite them, send this complete package to the HTTP handler, the handler try to read it with InputStreamReader, converts to characters and uses it as an argument to BufferedReader to improve performance and read using chunks for the subsequential reads

what are streams
- Collections -> store data
- Streams -> process and transform data like a pipeline
  -> needs a source of data (a collection for example)
  -> operates intermediary operations (filter, map, limit...)
  -> needs a terminate operation that usually returns a list for example (collect(toList())

streams are portals where the data will flow through
https://claude.ai/chat/c6764f14-d076-4e9a-93bc-23a09fe3a9c8

interfaces are a contract that will be implemented by a class.


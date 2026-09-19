# Three flow designs

`report.html` is a self-contained page comparing three implementations of the same
`flow.fear` API: the JDK-stream runtime on StandardLibrary `main`, the speculative
design of StandardLibrary PR 70 (`fearless70`), and the LegacyFearless push design
rebuilt as plain `rt` Java (`Legacy+`). It covers what each guarantees, where each
lets non-determinism through, the benchmark numbers, what each design taught the
other, and how to reconstruct the working copies.

Open it in a browser; it needs no server and loads nothing but its fonts.

`results.json` holds the benchmark numbers the page charts (ms per run and the JMH
99.9% error, per runtime). `jmh_log.txt` and `jmh_log_uneven.txt` are the raw JMH
output those numbers come from, produced by `RunBenchmarks.java`.

The same page is also published at https://claude.ai/artifact/LKZPJZsdKjWTNLAsswAcmU

Related pull requests: StandardLibrary 86 (determinism and termination tests),
87 (Legacy+), 88 (fearless70); Coordinator 159 (the integration tests), 160
(`RunBenchmarks.java` and the JMH jars); Controllers 33 (the ported benchmark suite).

(ns diman.core)

(defn view-matrix
  "View matrices."
  [M]
  (dorun (map println
              (conj M
                    (str "Size -> " (count M) " x " (count (first M)))))))




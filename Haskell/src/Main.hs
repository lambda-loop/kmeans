{-# LANGUAGE DataKinds #-}

module Main where

import System.Environment (getArgs)
import qualified Data.Text as T
import qualified Data.Text.IO as T
import qualified Data.Text.Read as TR
import qualified Data.Vector as VB
import Linear.V (V(..))
import Numeric.Natural (Natural)
import Serial (kMeans')

type V5 = V 5 Double

parseV5 :: T.Text -> Either String V5
parseV5 t =
  let fields = T.splitOn (T.pack ",") t
  in case traverse (TR.double . T.strip) fields of
      Right xs
        | length xs == 5 ->
            Right (V (VB.fromList (map fst xs)))
        | otherwise ->
            Left "Linha inválida: esperado 5 colunas"
      Left _ -> Left "Linha inválida: parse numérico falhou"

readCSV5 :: FilePath -> IO [V5]
readCSV5 fp = do
  ls <- T.lines <$> T.readFile fp
  case traverse parseV5 ls of
    Right ps -> pure ps
    Left e   -> fail e

main :: IO ()
main = do
  args <- getArgs
  case args of
    [file, kStr] -> do
      let k :: Natural
          k = read kStr
      pts <- readCSV5 file
      print (kMeans' k pts)
    _ -> putStrLn "Uso: cabal run k-means -- <arquivo.csv> <k>"
